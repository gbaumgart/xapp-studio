package pmedia.utils;
public final class BitUtils {

	public static int enableFlag(int enumValue){
		return (1<<enumValue);
	}
	
	public static Boolean hasFlag(int enumValue,int field)
	{
		int enumBitValue = 1<<field;
		int res=  (enumValue& enumBitValue);
		return  res!=0 ? true : false;
	}
	
	public static Boolean hasFlag(long enumValue,int field)
	{
		/**
		 * 		StaticWebContent:31,
                    StaticWebContentCategory:32,
		 */
		long test =1;
		long test2 = Long.rotateLeft(1, field);
		long enumBitValue = 1<<field;
		
		//long enumBitValue = 
		long res=  (enumValue&test2);
		return  res!=0 ? true : false;
	}
}