
package provider.disqus;

import java.util.ArrayList;
import java.util.List;

public class Response{
   	private String author;
   	private String category;
   	private String createdAt;
   	private int dislikes;
   	private String feed;
   	private String forum;
   	private String id;
   	private ArrayList identifiers;
   	private String ipAddress;
   	private boolean isClosed;
   	private boolean isDeleted;
   	private int likes;
   	private String link;
   	private String message;
   	private int posts;
   	private int reactions;
   	private String slug;
   	private String title;
   	private int userScore;
   	private boolean userSubscription;

 	public String getAuthor(){
		return this.author;
	}
	public void setAuthor(String author){
		this.author = author;
	}
 	public String getCategory(){
		return this.category;
	}
	public void setCategory(String category){
		this.category = category;
	}
 	public String getCreatedAt(){
		return this.createdAt;
	}
	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}
 	public int getDislikes(){
		return this.dislikes;
	}
	public void setDislikes(int dislikes){
		this.dislikes = dislikes;
	}
 	public String getFeed(){
		return this.feed;
	}
	public void setFeed(String feed){
		this.feed = feed;
	}
 	public String getForum(){
		return this.forum;
	}
	public void setForum(String forum){
		this.forum = forum;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public ArrayList getIdentifiers(){
		return this.identifiers;
	}
	public void setIdentifiers(ArrayList identifiers){
		this.identifiers = identifiers;
	}
 	public String getIpAddress(){
		return this.ipAddress;
	}
	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}
 	public boolean getIsClosed(){
		return this.isClosed;
	}
	public void setIsClosed(boolean isClosed){
		this.isClosed = isClosed;
	}
 	public boolean getIsDeleted(){
		return this.isDeleted;
	}
	public void setIsDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}
 	public int getLikes(){
		return this.likes;
	}
	public void setLikes(int likes){
		this.likes = likes;
	}
 	public String getLink(){
		return this.link;
	}
	public void setLink(String link){
		this.link = link;
	}
 	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message;
	}
 	public int getPosts(){
		return this.posts;
	}
	public void setPosts(int posts){
		this.posts = posts;
	}
 	public int getReactions(){
		return this.reactions;
	}
	public void setReactions(int reactions){
		this.reactions = reactions;
	}
 	public String getSlug(){
		return this.slug;
	}
	public void setSlug(String slug){
		this.slug = slug;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public int getUserScore(){
		return this.userScore;
	}
	public void setUserScore(int userScore){
		this.userScore = userScore;
	}
 	public boolean getUserSubscription(){
		return this.userSubscription;
	}
	public void setUserSubscription(boolean userSubscription){
		this.userSubscription = userSubscription;
	}
}
