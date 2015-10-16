package pmedia.types;

public class ArticleSection  extends Category
{
	@Override
	public CListItem getAsListItem()
	{
		CListItem item = super.getAsListItem();
		
		
		item.setDataClass("pmedia.types.ArticleCategory");
		item.setRefId(refId);
		getPicture();
		getPictures();
		getIconUrl();
		item.setIconUrl(getIconUrl());
		
		return item;
	}
}
