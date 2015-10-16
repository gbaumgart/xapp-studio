/*
* Copyright 2010 Srikanth Reddy Lingala  
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, 
* software distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
*/

package net.lingala.zip4j.crypto;

import java.util.ArrayList;
import java.util.Arrays;

import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.exception.ZipExceptionConstants;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class AESDecrypter implements IDecrypter {
	
	private LocalFileHeader localFileHeader;
	private AESEngine aesEngine;
	private MacBasedPRF mac;
	
	private final int PASSWORD_VERIFIER_LENGTH = 2;
	private int KEY_LENGTH;
	private int MAC_LENGTH;
	private int SALT_LENGTH;
	
	private byte[] aesKey;
	private byte[] macKey;
	private byte[] derivedPasswordVerifier;
	private byte[] storedMac;
	
	private int nonce = 1;
	
	public AESDecrypter(LocalFileHeader localFileHeader, 
			byte[] salt, byte[] passwordVerifier) throws ZipException {
		
		if (localFileHeader == null) {
			throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
		}
		
		this.localFileHeader = localFileHeader;
		this.storedMac = null;
		init(salt, passwordVerifier);
	}
	
	private void init(byte[] salt, byte[] passwordVerifier) throws ZipException {
		if (localFileHeader == null) {
			throw new ZipException("invalid file header in init method of AESDecryptor");
		}
		
		AESExtraDataRecord aesExtraDataRecord = localFileHeader.getAesExtraDataRecord();
		if (aesExtraDataRecord == null) {
			throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
		}
		
		switch (aesExtraDataRecord.getAesStrength()) {
		case Zip4jConstants.AES_STRENGTH_128:
			KEY_LENGTH = 16;
			MAC_LENGTH = 16;
			SALT_LENGTH = 8;
			break;
		case Zip4jConstants.AES_STRENGTH_192:
			KEY_LENGTH = 24;
			MAC_LENGTH = 24;
			SALT_LENGTH = 12;
			break;
		case Zip4jConstants.AES_STRENGTH_256:
			KEY_LENGTH = 32;
			MAC_LENGTH = 32;
			SALT_LENGTH = 16;
			break;
		default:
			throw new ZipException("invalid aes key strength for file: " + localFileHeader.getFileName());
		}
		
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(localFileHeader.getPassword())) {
			throw new ZipException("empty or null password provided for AES Decryptor");
		}
		
		byte[] derivedKey = deriveKey(salt, localFileHeader.getPassword());
		if (derivedKey == null || 
				derivedKey.length != (KEY_LENGTH + MAC_LENGTH + PASSWORD_VERIFIER_LENGTH)) {
			throw new ZipException("invalid derived key");
		}
		
		aesKey = new byte[KEY_LENGTH];
		macKey = new byte[MAC_LENGTH];
		derivedPasswordVerifier = new byte[PASSWORD_VERIFIER_LENGTH];
		
		System.arraycopy(derivedKey, 0, aesKey, 0, KEY_LENGTH);
		System.arraycopy(derivedKey, KEY_LENGTH, macKey, 0, MAC_LENGTH);
		System.arraycopy(derivedKey, KEY_LENGTH + MAC_LENGTH, derivedPasswordVerifier, 0, PASSWORD_VERIFIER_LENGTH);
		
		if (derivedPasswordVerifier == null) {
			throw new ZipException("invalid derived password verifier for AES");
		}
		
		if (!Arrays.equals(passwordVerifier, derivedPasswordVerifier)) {
			throw new ZipException("Wrong Password for file: " + localFileHeader.getFileName(), ZipExceptionConstants.WRONG_PASSWORD);
		}
		
		aesEngine = new AESEngine(aesKey);
		mac = new MacBasedPRF("HmacSHA1");
		mac.init(macKey);
	}
	
	public int decryptData(byte[] buff, int start, int len) throws ZipException {
		
		if (aesEngine == null) {
			throw new ZipException("AES not initialized properly");
		}
		
		try {
			//Split read buffer into BLOCK_SIZE byte blocks
			ArrayList byteBlocks = new ArrayList();
			ArrayList decBlocks = new ArrayList();
			for (int i = 0; i < len; i += InternalZipConstants.AES_BLOCK_SIZE) {
				byte[] block;
				if ((i + InternalZipConstants.AES_BLOCK_SIZE) > len) {
					block = new byte[len - i];
				} else {
					block = new byte[InternalZipConstants.AES_BLOCK_SIZE];
				}
				System.arraycopy(buff, i, block, 0, block.length);
				byteBlocks.add(block);
			}
			byte[] iv = new byte[InternalZipConstants.AES_BLOCK_SIZE];
			byte[] counterBlock = new byte[InternalZipConstants.AES_BLOCK_SIZE];
			for (int i = 0; i < byteBlocks.size(); i++) {
				byte[] cipherBlock = (byte[])byteBlocks.get(i);
				byte[] decryptedBlock = new byte[cipherBlock.length];
				
				mac.update(cipherBlock); //Update mac
				iv = Raw.toByteArray(nonce, InternalZipConstants.AES_BLOCK_SIZE);
				aesEngine.processBlock(iv, counterBlock);
				for (int j = 0; j < cipherBlock.length; j++) {
					decryptedBlock[j] = (byte)(cipherBlock[j] ^ counterBlock[j]);
				}
				decBlocks.add(decryptedBlock);
				nonce++;
			}
			
			//Join blocks
			int pos = 0;
			for (int i = 0; i < decBlocks.size(); i++) {
				System.arraycopy(decBlocks.get(i), 0, buff, pos, ((byte[])decBlocks.get(i)).length);
				pos+=((byte[])decBlocks.get(i)).length;
			}
			return len;
			
		} catch (ZipException e) {
			throw e;
		} catch (Exception e) {
			throw new ZipException(e);
		}
	}
	
	public int decryptData(byte[] buff) throws ZipException {
		return decryptData(buff, 0, buff.length);
	}
	
	private byte[] deriveKey(byte[] salt, String password) throws ZipException {
		try {
			PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1",
	                    salt, 1000);
	        PBKDF2Engine e = new PBKDF2Engine(p);
	        byte[] derivedKey = e.deriveKey(password, KEY_LENGTH + MAC_LENGTH + PASSWORD_VERIFIER_LENGTH);
			return derivedKey;
		} catch (Exception e) {
			throw new ZipException(e);
		}
	}
	
	public int getPasswordVerifierLength() {
		return PASSWORD_VERIFIER_LENGTH;
	}
	
	public int getSaltLength() {
		return SALT_LENGTH;
	}
	
	public byte[] getCalculatedAuthenticationBytes() {
		return mac.doFinal();
	}
	
	public void setStoredMac(byte[] storedMac) {
		this.storedMac = storedMac;
	}

	public byte[] getStoredMac() {
		return storedMac;
	}

//	public byte[] getStoredMac() throws ZipException {
//		if (raf == null) {
//			throw new ZipException("attempting to read MAC on closed file handle");
//		}
//		
//		try {
//			byte[] storedMacBytes = new byte[InternalZipConstants.AES_AUTH_LENGTH];
//			int bytesRead = raf.read(storedMacBytes);
//			if (bytesRead != InternalZipConstants.AES_AUTH_LENGTH) {
//				if (zipModel.isSplitArchive()) {
////					unzipEngine.startNextSplitFile();
//					if (bytesRead == -1) bytesRead = 0;
//					int newlyRead = raf.read(storedMacBytes, bytesRead, InternalZipConstants.AES_AUTH_LENGTH - bytesRead);
//					bytesRead += newlyRead;
//					if (bytesRead != InternalZipConstants.AES_AUTH_LENGTH) {
//						throw new ZipException("invalid number of bytes read for stored MAC after starting split file");
//					}
//				} else {
//					throw new ZipException("invalid number of bytes read for stored MAC");
//				}
//			}
//			return storedMacBytes;
//		} catch (IOException e) {
//			throw new ZipException(e);
//		} catch (Exception e) {
//			throw new ZipException(e);
//		}
//		
//	}
}
