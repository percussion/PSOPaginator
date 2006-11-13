/******************************************************************************
 *
 * [ rx_ephox.js ]
 *
 * COPYRIGHT (c) 1999 - 2005 by Percussion Software, Inc., Woburn, MA USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Percussion.
 * 
 *
 ******************************************************************************/

/**
 * This file holds the functions for the CMS buttons on the EditLive control.
 */

/**
 * Constant: Holds url for search page.  Called by child popup pages.
 */
 var INLINE_SEARCH_PAGE = "../sys_searchSupport/getQuery.html?";

/**
 * Constant: Holds url for return page.  Called by child popup pages.
 */
 var INLINE_RETURN_PAGE =  "../sys_searchSupport/getResults.html?";

/**
 * Constant: Holds url for EditLive help directory.
 */
 var HELP_DIR =  "../rx_resources/ephox/enduserhelp/";
 
 /**
  * A flag indicating that this is EditLive. Used by returnVariant.xsl
  */
 var isEditLive = true;

/**
 * A flag indicating, whether or not the current browser type is Netscape. 
 * <code>true</code> if Netscape, false otherwise.
 */
var isNav = false;

/**
 * A flag indicating, whether or not the current browser type is Internet Explorer. 
 * <code>true</code> if Internet Explorer, false otherwise.
 */
var isIE  = false;

/**
 * A flag indicating, whether or not the OS is Macintosh. 
 * <code>true</code> if Macintosh, false otherwise.
 */
var isMac = false;

/**
 * Check the browser and set appropriate member field.  
 * Assume Netscape or IE only.
 */
if (navigator.appName == "Netscape") 
{
   isNav = true;
}
else
{
   isIE = true;
}

/**
 * Check the OS and set appropriate member field.  Note: We will need this 
 * in next upgrade.
 */
if (navigator.platform == "MacPPC")
{
   isMac = true;
}

/**
 * Data Object.  Holds property values written to maintain state.
 */
var dataObject = new Object();
dataObject.returnedValue = "";
dataObject.sEditorName = "";
dataObject.wepSelectedText = "";
dataObject.searchType = "";
dataObject.windowRef = "";
dataObject.editorObject = ""; 


var ephoxText = ""; 
function ephoxSelectedText(incoming)
{
    ephoxText = incoming; 
}
/**
* The array of all editors on the page.  There will be one entry per editor...
*
*  var editor = new Object();
*  editor.objectref = eopObject
*  editor.name = field name
*  editor.inlineLinkSlot = "";
*  editor.inlineImageSlot = "";
*  editor.inlineVariantSlot = ""; 
* 
*/

var _rxAllEditors = new Array();

function getEditor(sEditorName) 
{
   
   for(var i=0; i < _rxAllEditors.length; i++)
     {
       if(_rxAllEditors[i].name == sEditorName)
          return(_rxAllEditors[i]);
     }
   alert("Editor " + sEditorName + " not found!!!!"); 
   return("");  
}
/** 
* CMS Link function 
* 
*/

/**
 * Creates CMS search box for inline links and CMS Image creation:
 */
function createSearchBox(inlineslotid, sEditorName, type) 
{
   
   var editor = getEditor(sEditorName);
   var ephox = editor.objectref; 
 
   dataObject.editorObject = ephox; 
   dataObject.searchType = type; 
   dataObject.slotid = inlineslotid; 
   
   ephox.GetSelectedText("ephoxSelectedText");
   setTimeout("launchSearchBox()",350); 
}

function launchSearchBox()
   {
   var selectedText =  ephoxText; 
   var inlineslotid = dataObject.slotid;
  
   //Update the inlinelinksearch form elements
   document.inlinelinkssearch.action = INLINE_SEARCH_PAGE;
   document.inlinelinkssearch.inlinetext.value = selectedText;
   document.inlinelinkssearch.inlineslotid.value = inlineslotid;
   document.inlinelinkssearch.inlinetype.value = dataObject.searchType;
   //Open an empty window.
   var w = "";
   if(isNav)
   {
      dataObject.windowRef = window.open("", "searchitems", 
      "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1," + 
      "resizable=1,width=400,height=400,screenX=220,screenY=220");
   } 
   else
   {
      dataObject.windowRef =  window.open("", "searchitems", 
      "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1," + 
      "resizable=1,width=400,height=400, left=220, top=220");
   }   
   //Submit the inlinelinksearch form to this window
   document.inlinelinkssearch.submit();

   dataObject.windowRef.focus();
  
}

/**
 * Clean up dataObject.  Set all properties to "" for the next call. 
 */
function cleanUp()
{
   dataObject.returnedValue = "";
   dataObject.sEditorName = "";
   dataObject.wepSelectedText = "";
   dataObject.searchType = "";
   dataObject.windowRef = "";

}

/**
 * Formats the output then pastes into the control.
 * Netscape 4.7 & 6.23 compliant.  
 * @param returnedHTML is a string and can be <code>null</code>
 */
function formatOutput(returnedHTML)
{   
   dataObject.searchType = "";
   //alert("Returned HTML is: " + returnedHTML); 
   var ephox = dataObject.editorObject;
   ephox.InsertHTMLAtCursor(escapeSpecial(returnedHTML));  
}

/**
 * Builds a url from the parameters given. 
 * @param oStr -  is the text in the browser.  May be <code>null</code>.
 * @param str - is the url text.  May be <code>null</code>.
 */
function buildUrl(oStr, str)
{  
   if(!isStringValid(str))
   {
      return;
   }

   var link = "<A HREF=\""+ str +"\">"
   
   if(!isStringValid(oStr))
   {
     link += str;
   }  
   else 
   {
      link += oStr;
   }            
    
   link += "</A>";   

   var ephox = dataObject.editorObject;
   ephox.InsertHTMLAtCursor(escapeSpecial(link));  
}  
  
/**
 * Does str pass our test for valid.  Returns <code>true</code> if 
 * it does.
 * @param str - string to be tested. May be <code>null</code>.
 */
function isStringValid(str)
{      
   if(str == null || str.length == 0 || str == "http://")
   {
      return false;
   }

   return true;
}



/**
 * Creates link to non Rhythmyx managed element.
 * @param sEditorName - must not be <code>null</code> or empty.
 */
function createExternalReference(sEditorName)
{
   if(sEditorName == null || sEditorName.length == 0)
   {
      alert("Please notify the Rhythmyx administrator: " + 
      "ERROR: createExternalReference - received null values");
      return;
   } 
   var editor = getEditor(sEditorName);
   var ephox = editor.objectref; 
 
   dataObject.editorObject = ephox; 
   ephox.GetSelectedText("ephoxSelectedText");
   setTimeout("launchPromptBox()",350); 
}

/**
 * Inserts an empty <p> tag set either before or after the
 * existing content.
 * @param sEditorName - must not be <code>null</code> or empty.
 * @param isPreContent - determines if the P tags come before
 * or after the existing content.
 */
function _insertPTags(sEditorName, isPreContent)
{
   if(sEditorName == null || sEditorName.length == 0)
      {
         alert("Please notify the Rhythmyx administrator: " + 
         "ERROR: _insertPTags - received null values");
         return;
      } 
      var editor = getEditor(sEditorName);
      var ephox = editor.objectref; 
    
   dataObject.editorObject = ephox;
   ephox.GetBody('ephoxSelectedText');
   setTimeout("_insertPTagsPart2("+ isPreContent +")",350);
}

function _insertPTagsPart2(isPreContent)
{
   var selectedText =  ephoxText;
   var ephox = dataObject.editorObject;
   var ptags = "<p></p>";
   var modifedContent = "";
   if(isPreContent)
      modifiedContent = ptags + ephoxText;
   else
      modifiedContent = ephoxText + ptags;
   ephox.setBody(escape(modifiedContent));   
}

function _insertPageBreak(sEditorName, isPreContent)
{
   if(sEditorName == null || sEditorName.length == 0)
      {
         alert("Please notify the Rhythmyx administrator: " + 
         "ERROR: _insertPageBreak - received null values");
         return;
      } 
      var editor = getEditor(sEditorName);
      var ephox = editor.objectref; 
    
   dataObject.editorObject = ephox;
   ephox.InsertHTMLAtCursor("<?pageBreak?>");
}



function launchPromptBox()
{
   var selectedText =  ephoxText; 

  
   var str=prompt("Enter link location (e.g. http://www.yahoo.com):", "http:\/\/");
   buildUrl(selectedText, str );
}
// A list of allowed elements, these are defined from HTML 4.01
var allowed = new Array("sub","sup","small","big","em","b","i","tt","strong","dfn","code",
"samp","kbd","var","cite","abbr","acronym","img","object","br","script","map","q","span","bdo","font");

var skipFirst = new Array("p","div");

// Anchor tags only allow certain elements to be nested inside them. This function
// determines if the selectedHtml only has allowable elements. The list of allowable
// elements is listed in the "allowed" variable above.
//
// There are some exceptions. If the first element found is one of the "skipFirst"
// elements listed, those are allowed for that special case. This allows a user to 
// do what looks like a valid selection that really includes (incorrectly) the div
// or paragraph tags. In this circumstance, ektron will fix the results.
function isStringValidForLink(selectedHtml)
{
	var i;
	var len = selectedHtml.length;

	// Handle some special cases.. notably Ektron can return <p>content</p> or <div>content</div> when
	// the user believes they've just selected regular text or an image. Skip these starting cases
	var first = true;

	for(i = 0; i < len; i++)
	{
		// If we find an element that is not in the allowed array, then return false
		if (selectedHtml.charAt(i) == '<')
		{
			// Find the end delimiter or space
			var e, j;
			for(e = i + 1; e < len; e++)
			{
				var ch = selectedHtml.charAt(e);
				if (ch == '>' || ch == ' ')
				{
					break;
				}
			}
			var el = selectedHtml.substring(i + 1, e).toLowerCase();
			var inarray = false;
			if (first)
			{
				first = false;
				for(j = 0; j < skipFirst.length; j++)
				{
					if (skipFirst[j] == el)
					{
						inarray = true;
					}
				}
			}
			if (el.length > 0 && el.charAt(0) == '/')
			{
				inarray = true; // Closing tag, ignore
			}
			for(j = 0; j < allowed.length && inarray == false; j++)
			{
				if (allowed[j] == el)
				{
					inarray = true;
				}
			}
			if (! inarray)
			{
				return false;
			}
			i = e;
		}
	}

	return true;
}

function RxEphoxHelp()
{
   var lang = document.forms['EditForm'].sys_lang.value;
   var langCode = lang.substr(0, 2);
   var helpLang = "english";
   
   /* Uncomment when we implement help in other languages
   if(langCode == "cs") helpLang = "czech";
   if(langCode == "fr") helpLang = "french";
   if(langCode == "de") helpLang = "german";
   if(langCode == "it") helpLang = "italian";
   if(langCode == "ko") helpLang = "korean";
   if(langCode == "es") helpLang = "spanish";
   */
   var helpURL = HELP_DIR + helpLang + "/userhelphome.htm";
   
   var nWin = window.open(helpURL, "ephoxHelp", "height=600; width=800; menubar=0; status=0; toolbar=0");
   nWin.focus();
   
}


function isEphoxControlDirty(sEditorName)
{
   
    var ephoxApplet = PSGetApplet(self, sEditorName + "_elj");
    return ephoxApplet.isDirty();	
  
}

/*
 EditLive seems to have a problem with %a0 which is the 
 URL encoding value of a non breaking space. This function
 will handle that case by replacing the %a0 with %26nbsp;
 which is really &nbsp;
 */
function escapeSpecial(str)
{
   var encoded = escape(str);
   encoded = encoded.replace(/\%a0/ig, "%26nbsp;");
   return encoded;

}

/*
  Loops through the EditLive editor array and moves the controls contents
  into each appropriate hidden field.
*/
function rxEphoxPreSubmit()
{
   
   var editorName = "";
   var ephoxApplet = null;
   var hiddenField = null;
   for(var i=0; i < _rxAllEditors.length; i++)
   {
          editorName = _rxAllEditors[i].name;
          // Get applet
          ephoxApplet = PSGetApplet(self, editorName + "_elj");
          if(ephoxApplet == null || ephoxApplet == undefined)
          {
             alert("Unable to retrieve EditLive Applet: " + editorName + "_elj.");
          }
          else
          {
             //Get hidden field and set value
             hiddenField = document.getElementById(editorName);
             if(hiddenField == null || hiddenField == undefined)
	     {
	        alert("Unable to retrieve EditLive hidden field: " + editorName + ".");
             }
             else
             {
                hiddenField.value = ephoxApplet.getDocument();
             }
          }
   }
}
 
/*
  Disables the submit button on the CE
 */
function rxEphoxDisableSubmit()
{
   var submitButton = document.getElementById("rxCESubmit");
   if(submitButton != null && submitButton != undefined)
      submitButton.disabled = true;
}

/*
 Tracks the number of ephox controls that completed
 init and if all are inited then enables the submit
 button
 */
var _initedEditLiveControls = 0; 
function rxEphoxHandleEditorInitComplete()
{
   ++_initedEditLiveControls;
   if(_rxAllEditors.length == _initedEditLiveControls)
   {
      var submitButton = document.getElementById("rxCESubmit");
      if(submitButton != null && submitButton != undefined)
         submitButton.disabled = false;
   }
}
 