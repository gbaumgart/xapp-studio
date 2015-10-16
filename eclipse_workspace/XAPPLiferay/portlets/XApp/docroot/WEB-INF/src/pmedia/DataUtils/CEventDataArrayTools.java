package pmedia.DataUtils;

import java.util.ArrayList;
import java.util.Vector;

import pmedia.types.CContent;
import pmedia.types.EventData;

public class CEventDataArrayTools
{

	public static void sort(Vector<CContent> a)
	{
	
		if(a==null || a.size()==0)
			return;
	  for(int i=0; i<a.size()-1; i++)
	  {
	      int pos = findMinPos(a,i);
	      swap(a,i,pos);
	     }
	 }
	private static void swap(Vector<CContent>a,int pos1,int pos2)
	 {
		CContent temp = a.get(pos1);
		a.set(pos1, a.get(pos2));
		a.set(pos2, temp);
	 }
	private static int findMinPos(Vector<CContent> a,int pos)
	{
	  for(int i=pos+1; i<a.size(); i++)
	     if(a.get(i).getStartDate().compareTo(a.get(pos).getStartDate())<0)
	        pos=i;
	  return pos;
	 }
	
	public static void sort(ArrayList<CContent> a)
	{
	  for(int i=0; i<a.size()-1; i++)
	  {
	      int pos = findMinPos(a,i);
	      swap(a,i,pos);
	     }
	 }
	private static void swap(ArrayList<CContent>a,int pos1,int pos2)
	 {
		CContent temp = a.get(pos1);
		a.set(pos1, a.get(pos2));
		//a[pos1]=a[pos2];
		//a[pos2]=temp;
		a.set(pos2, temp);
	 }
	private static int findMinPos(ArrayList<CContent> a,int pos)
	{
	  for(int i=pos+1; i<a.size(); i++)
	     if(a.get(i).startDate.compareTo(a.get(pos).getStartDate())<0)
	        pos=i;
	  return pos;
	 }
	public static ArrayList<CContent>sortByDate(ArrayList<CContent>src)
	{
		
		CContent res[] = new CContent[src.size()]; 
		   //EventData number[] = new EventData[src.size()];
		   int nElems = src.size();
		   int in, out;
		   for (out = 0; out < nElems; out++) 
		   {
			   CContent temp = src.get(out);
			   in = out;
			   CContent tempA = src.get(in -1);
			   if(temp!=null && tempA!=null)
			   {
				   //int compare = tempA.getStart_time().compareTo(temp.getStart_time());
				   //System.out.println("comparing  tempA : " + tempA.getStart_time() + " with : " + temp.getStart_time() + "  = " +  compare);
				   
			   }else
			   {
				   System.out.println("have null element");
			   }
			   try {
				   while (in > 0 && (src.get(in - 1).getStartDate().compareTo(temp.getStartDate())) > 0) 
				   {
				        res[in] = res[in - 1];
				        --in;
				   }
					
			   } catch (Exception e) {
						// TODO: handle exception
				   System.out.println("crash");
				   e.printStackTrace();
				   continue;
				
			   }
			   
		      res[in] = temp;
			}

		   ArrayList<CContent>result = new ArrayList<CContent>();
		   for (out = 0; out < nElems; out++) 
		   {
			   CContent temp = res[out];
			   if(temp!=null)
				   result.add(temp);
		   }
		   
		   return result;
	   }
	public static ArrayList<EventData>sortByDate(Vector<EventData>src)
	{
		   EventData res[] = new EventData[src.size()]; 
		   //EventData number[] = new EventData[src.size()];
		   int nElems = src.size();
		   int in, out;
		   for (out = 1; out < nElems; out++) 
		   {
			   EventData temp = src.get(out);
			   in = out;
			   EventData tempA = src.get(in -1);
			   if(temp!=null && tempA!=null)
			   {
				   
				   int compare = tempA.getStart_time().compareTo(temp.getStart_time());
				   //System.out.println("comparing  tempA : " + tempA.getStart_time() + " with : " + temp.getStart_time() + "  = " +  compare);
				   
			   }else
			   {
				   System.out.println("have null element");
			   }

			   while (in > 0 && src.get(in - 1).getStart_time().compareTo(temp.getStart_time()) > 0) 
			   {
			        res[in] = res[in - 1];
			        --in;
			   }
		      res[in] = temp;
			}
		//return res;
		   
		   ArrayList<EventData>result = new ArrayList<EventData>();
		   for (out = 0; out < nElems; out++) 
		   {
			   EventData temp = res[out];
			   if(temp!=null)
				   result.add(temp);
		   }
		   
		   return result;
	   }
}

