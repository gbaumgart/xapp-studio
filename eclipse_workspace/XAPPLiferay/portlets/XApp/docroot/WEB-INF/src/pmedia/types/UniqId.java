package pmedia.types;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class UniqId {
    //private static final Logger log    = Logger.getLogger(UniqId.class);
    private static char[]       digits = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static UniqId me       = new UniqId();
    private String        hostAddr;
    private Random        random  = new SecureRandom();
    private MessageDigest mHasher;
    private UniqTimer     timer   = new UniqTimer();
    private ReentrantLock opLock = new ReentrantLock();

    
    private UniqId() {
        try {
            InetAddress addr = InetAddress.getLocalHost();

            hostAddr = addr.getHostAddress();
        } catch (IOException e) {
            //log.error("[UniqID] Get HostAddr Error", e);
            hostAddr = String.valueOf(System.currentTimeMillis());
        }

        if (null == hostAddr || hostAddr.trim().length() == 0
                        || "127.0.0.1".equals(hostAddr)) {
                hostAddr = String.valueOf(System.currentTimeMillis());
        }

        try {
            mHasher = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nex) {
            mHasher = null;
            //log.error("[UniqID]new MD5 Hasher error", nex);
        }
    }

    /**
     *
     * @return UniqId
     */
    public static UniqId getInstance() {
        return me;
    }
    
    /**
     * 
     */
    public long getUniqTime() {
        return timer.getCurrentTime();
    }

    /**
     * @return uniqTime-randomNum-hostAddr-threadId
     */
    public String getUniqID() {
        
    	StringBuffer sb = new StringBuffer();
        long         t = timer.getCurrentTime();

        sb.append(t);

        sb.append("-");

        sb.append(random.nextInt(8999) + 1000);

        sb.append("-");
        sb.append(hostAddr);

        sb.append("-");
        sb.append(Thread.currentThread().hashCode());

        /*if (log.isDebugEnabled()) {
            log.debug("[UniqID.getUniqID]" + sb.toString());
        }*/

        return sb.toString();
    }

    /**
     * 
     * @return uniqId md5 string
     */
    public String getUniqIDHashString() {
        return hashString(getUniqID());
    }
    
    /**
     * 
     * @return uniqId md5 byte[16]
     */
    public byte[] getUniqIDHash() {
        return hash(getUniqID());
    }
    
    /**
     * @param str
     * @return md5 byte[16]
     */
    public byte[] hash(String str) 
    {
        opLock.lock();
        
        try {
                byte[] bt = mHasher.digest(str.getBytes());
                if (null == bt || bt.length != 16) {
                        throw new IllegalArgumentException("md5 need");
                }
                return bt;
        }finally {
                opLock.unlock();
        }
    }

    /**
     * @param str
     * @return md5 string
     */
    public String hashString(String str) {
        byte[] bt = hash(str);
        return bytes2string(bt);
    }

        /**
         */
        public String bytes2string(byte[] bt) {
                int    l = bt.length;

        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++]     = digits[(0xF0 & bt[i]) >>> 4];
            out[j++]     = digits[0x0F & bt[i]];
        }

        /*
        if (log.isDebugEnabled()) {
            log.debug("[UniqID.hash]" + (new String(out)));
        }
        */
        
        return new String(out);
        }

    /**
     * @author dogun
     */
    private class UniqTimer 
    {
        private AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());

        public long getCurrentTime() {
            return this.lastTime.incrementAndGet();
        }
    }
}
