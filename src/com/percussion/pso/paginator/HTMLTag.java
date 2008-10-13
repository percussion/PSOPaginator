package com.percussion.pso.paginator;

public class HTMLTag 
{	
	private String tagName = "";
	private String tagText = "";
	private int openedInPage = 0; // Page number where the tag was  opened
	private int closedInPage = 0;  // Page number where the tag was closed
	private boolean empty = true; //does tag have content 
	/**
	 * @return Returns the tagName.
	 */
	public String getTagName() {
		return tagName;
	}
	/**
	 * @param tagName The tagName to set.
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	/**
	 * @return Returns the tagText.
	 */
	public String getTagText() {
		return tagText;
	}
	/**
	 * @param tagText The tagText to set.
	 */
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	/**
	 * @return Returns the closedInPage.
	 */
	public int getClosedInPage() {
		return closedInPage;
	}
	/**
	 * @param closedInPage The closedInPage to set.
	 */
	public void setClosedInPage(int closedInPage) {
		this.closedInPage = closedInPage;
	}
	/**
	 * @return Returns the openedInPage.
	 */
	public int getOpenedInPage() {
		return openedInPage;
	}
	/**
	 * @param openedInPage The openedInPage to set.
	 */
	public void setOpenedInPage(int openedInPage) {
		this.openedInPage = openedInPage;
	}
   /**
    * @return the empty
    */
   public boolean isEmpty()
   {
      return empty;
   }
   /**
    * @param empty the empty to set
    */
   public void setEmpty(boolean empty)
   {
      this.empty = empty;
   }
	
}
