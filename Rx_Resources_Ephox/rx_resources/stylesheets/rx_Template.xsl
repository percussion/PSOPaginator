<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE xsl:stylesheet [
	<!ENTITY % HTMLlat1 SYSTEM "/Rhythmyx/DTD/HTMLlat1x.ent">
		%HTMLlat1;
	<!ENTITY % HTMLsymbol SYSTEM "/Rhythmyx/DTD/HTMLsymbolx.ent">
		%HTMLsymbol;
	<!ENTITY % HTMLspecial SYSTEM "/Rhythmyx/DTD/HTMLspecialx.ent">
		%HTMLspecial;
]>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:psxctl="URN:percussion.com/control" xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="psxi18n" xmlns:psxi18n="urn:www.percussion.com/i18n" >
<xsl:template match="/" />
	<!--
     sys_FileWord. 
     Do not modify this control directly. This control, word template file and cab files need to be modified together.
     Please see read me or help for upgrading the word controls. 
 -->
	<psxctl:ControlMeta name="sys_FileWord" dimension="single" choiceset="none">
		<psxctl:Description>a file upload input control with MS Word launcher</psxctl:Description>
		<psxctl:ParamList>
			<psxctl:Param name="id" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter assigns a name to an element. This name must be unique in a document.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="class" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter assigns a class name or set of class names to an element. Any number of elements may be assigned the same class name or names. Multiple class names must be separated by white space characters.  The default value is "datadisplay".</psxctl:Description>
				<psxctl:DefaultValue>datadisplay</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="style" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies style information for the current element. The syntax of the value of the style attribute is determined by the default style sheet language.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="size" datatype="Number" paramtype="generic">
				<psxctl:Description>This parameter tells the user agent the initial width of the control. The width is given in pixels. The default value is 50.</psxctl:Description>
				<psxctl:DefaultValue>50</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="tabindex" datatype="Number" paramtype="generic">
				<psxctl:Description>This parameter specifies the position of the current element in the tabbing order for the current document. This value must be a number between 0 and 32767.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="cleartext" datatype="String" paramtype="custom">
				<psxctl:Description>This parameter determines the text that will be displayed along with a checkbox when the field supports being cleared.  The default value is 'Clear Word'.</psxctl:Description>
				<psxctl:DefaultValue>Clear Word</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="RxContentEditorURL" datatype="String" paramtype="msword">
				<psxctl:Description>This parameter specifies the absolute URL to the content editor of the current content item.  The value is passed to the OCX control, which uses it to obtain the metadata fields of the current content item.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="WordTemplateURL" datatype="String" paramtype="msword">
				<psxctl:Description>This parameter specifies the absolute URL to retrieve the Microsoft Word template document which provides the macros used to edit content items within Word.  The value is passed to the OCX control.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="ContentBodyURL" datatype="String" paramtype="msword">
				<psxctl:Description>This parameter specifies the absolute URL to retrieve the Microsoft Word document associated with the current content item.  The value is passed to the OCX control.</psxctl:Description>
			</psxctl:Param>
			<psxctl:Param name="InlineLinkSlot" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the id of inline link slot. The inline search dialog box shows the content types that have at least one variant added to the inline link slot. The default value is system inline link slotid 103.</psxctl:Description>
				<psxctl:DefaultValue>103</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="InlineImageSlot" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the id of inline image slot. The inline search dialog box shows the content types that have at least one variant added to the inline image slot. The default value is system inline image slotid 104.</psxctl:Description>
				<psxctl:DefaultValue>104</psxctl:DefaultValue>
			</psxctl:Param>
		</psxctl:ParamList>
		<psxctl:Dependencies>
			<psxctl:Dependency status="readyToGo" occurrence="single">
				<psxctl:Default>
					<PSXExtensionCall id="0">
						<name>Java/global/percussion/generic/sys_FileInfo</name>
					</PSXExtensionCall>
				</psxctl:Default>
			</psxctl:Dependency>
		</psxctl:Dependencies>
	</psxctl:ControlMeta>
	
	
	
	
	
	
	
   <!-- 
     sys_EditLive
     -->
	<psxctl:ControlMeta name="sys_EditLive" dimension="single" choiceset="none">
		<psxctl:Description>Ephox EditLive HTML Editor</psxctl:Description>
		<psxctl:ParamList>
			<psxctl:Param name="width" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the width of the inline frame. This parameter may be either a pixel or a percentage of the available horizontal space. The default value is "700".</psxctl:Description>
				<psxctl:DefaultValue>760</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="height" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the height of the inline frame. This parameter may be either a pixel or a percentage of the available vertical space. The default value is 250.</psxctl:Description>
				<psxctl:DefaultValue>250</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="config_src_url" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the location of the config.xml that will the control will use for configuration. This file must be in the /rx_resources/ephox folder.  The default value is "config.xml".</psxctl:Description>
				<psxctl:DefaultValue>../rx_resources/ephox/elj_config.xml</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="config_download" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the location of the download directory The default value is "../rx_resources/ephox/editlivejava".</psxctl:Description>
				<psxctl:DefaultValue>../rx_resources/ephox/editlivejava</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="InlineLinkSlot" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the id of inline link slot. The inline search dialog box shows the content types that have at least one variant added to the inline link slot. The default value is system inline link slotid 103.</psxctl:Description>
				<psxctl:DefaultValue>103</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="InlineImageSlot" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the id of inline image slot. The inline search dialog box shows the content types that have at least one variant added to the inline image slot. The default value is system inline image slotid 104.</psxctl:Description>
				<psxctl:DefaultValue>104</psxctl:DefaultValue>
			</psxctl:Param>
			<psxctl:Param name="InlineVariantSlot" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the id of inline variant slot. The inline search dialog box shows the content types that have at least one variant added to the inline variant slot. The default value is system inline variant slotid 105.</psxctl:Description>
				<psxctl:DefaultValue>105</psxctl:DefaultValue>
			</psxctl:Param>
                        <psxctl:Param name="DebugLevel" datatype="String" paramtype="generic">
				<psxctl:Description>This parameter specifies the debug level for the EditLive Applet. The allowed levels are (fatal, error, warn, info, debug, http) </psxctl:Description>
				<psxctl:DefaultValue>info</psxctl:DefaultValue>
			</psxctl:Param>			
		</psxctl:ParamList>
		<psxctl:AssociatedFileList>
			<psxctl:FileDescriptor name="editlivejava.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../rx_resources/ephox/editlivejava/editlivejava.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="rx_ephox.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../rx_resources/ephox/rx_ephox.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
		</psxctl:AssociatedFileList>
		<psxctl:Dependencies>
			<psxctl:Dependency status="setupOptional" occurrence="multiple">
				<psxctl:Default>
					<PSXExtensionCall id="0">
						<name>Java/global/percussion/xmldom/sys_xdTextCleanup</name>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text>$(fieldName)</text>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text>rxW2Ktidy.properties</text>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text>rxW2KserverPageTags.xml</text>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text/>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text/>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
						<PSXExtensionParamValue id="0">
							<value>
								<PSXTextLiteral id="0">
									<text>yes</text>
								</PSXTextLiteral>
							</value>
						</PSXExtensionParamValue>
					</PSXExtensionCall>
				</psxctl:Default>
			</psxctl:Dependency>
			
		</psxctl:Dependencies>
	</psxctl:ControlMeta>
        <xsl:template match="Control[@name='sys_EditLive']" mode="psxcontrol-customcontrol-isdirty">		
	   <xsl:value-of select="concat(' || CustomControlIsDirty_',@paramName,'()')"/>			
	</xsl:template>
	<xsl:template match="Control[@name='sys_EditLive']" priority="10" mode="psxcontrol">
		<xsl:variable name="name">
			<xsl:value-of select="@paramName"/>
		</xsl:variable>
		<xsl:variable name="width">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='width']">
					<xsl:value-of select="ParamList/Param[@name='width']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='width']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="height">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='height']">
					<xsl:value-of select="ParamList/Param[@name='height']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='height']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="config_src_url">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='config_src_url']">
					<xsl:value-of select="ParamList/Param[@name='config_src_url']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='config_src_url']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
				<xsl:variable name="config_download">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='config_download']">
					<xsl:value-of select="ParamList/Param[@name='config_download']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='config_download']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="InlineLinkSlot">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='InlineLinkSlot']">
					<xsl:value-of select="ParamList/Param[@name='InlineLinkSlot']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='InlineLinkSlot']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="InlineImageSlot">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='InlineImageSlot']">
					<xsl:value-of select="ParamList/Param[@name='InlineImageSlot']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='InlineImageSlot']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="InlineVariantSlot">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='InlineVariantSlot']">
					<xsl:value-of select="ParamList/Param[@name='InlineVariantSlot']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='InlineVariantSlot']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="DebugLevel">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='DebugLevel']">
					<xsl:value-of select="ParamList/Param[@name='DebugLevel']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="document('')/*/psxctl:ControlMeta[@name='sys_EditLive']/psxctl:ParamList/psxctl:Param[@name='DebugLevel']/psxctl:DefaultValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>		
		<xsl:variable name="encodedConfigUrl" xmlns:URLEncoder="java:java.net.URLEncoder"
		   select="URLEncoder:encode($config_src_url,'UTF8')"/>
      <xsl:variable name="validItemLocale">
         <xsl:choose>
            <xsl:when test="not($itemLocale = '')">
               <xsl:value-of select="$itemLocale"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$lang"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <xsl:variable name="childkeyid" select="../../@childkey"/>
      <xsl:variable name="uniqueName" select="concat($name, $childkeyid)"/>
      <xsl:variable name="config_app_url">../sys_Ephox_support/config.xml&#063;config=<xsl:value-of select="$encodedConfigUrl"/>&amp;field=<xsl:value-of select="$uniqueName"/>&amp;sys_lang=<xsl:value-of select="$validItemLocale"/>&amp;isReadOnly=<xsl:value-of select="@isReadOnly"/>
      </xsl:variable>
	 <xsl:variable name="EncodedValue"  xmlns:PSEphoxUtil="java:com.percussion.cms.ephox.PSEphoxUtil"
              select="PSEphoxUtil:encodeContent(Value,@isReadOnly)" /> 
              
      <input type="hidden" name="{$uniqueName}_temp" value="{$EncodedValue}"/>
      <input type="hidden" name="{$uniqueName}" id="{$uniqueName}"/>
	 <!-- now comes the actual script -->	
	<script language="JavaScript">
			<![CDATA[
               var ePhox; 
               var ps_hasEPhox; 
               ePhox = new EditLiveJava("]]><xsl:value-of select="$uniqueName"/><![CDATA[", "]]><xsl:value-of select="$width"/><![CDATA[", "]]><xsl:value-of select="$height"/><![CDATA["); ]]>

        <![CDATA[
        function CustomControlIsDirty_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
        {
           var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA[";
           return isEphoxControlDirty(EditorName);
        }
        
      	//control functions for inline links.  Launches the search box.  see rx_ephox.js 
      	function RxEphoxInlineLink_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
      	{
                var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
                var SlotId = "]]><xsl:value-of select="$InlineLinkSlot" /><![CDATA[";
                createSearchBox(SlotId, EditorName, "rxhyperlink"); 
      	}
      	function RxEphoxImageLink_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
      	{
                var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
                var SlotId = "]]><xsl:value-of select="$InlineImageSlot" /><![CDATA[";
                createSearchBox(SlotId, EditorName, "rximage"); 
      	}
    		function RxEphoxVariantLink_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
      	{
                var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
                var SlotId = "]]><xsl:value-of select="$InlineVariantSlot" /><![CDATA[";
                createSearchBox(SlotId, EditorName, "rxvariant"); 
      	}

          function RxEphoxWebLink_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
      	{
                var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
                createExternalReference(EditorName); 
      	}
      	
      	function RxEphoxInsertPTagsA_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
      	{
                var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
                _insertPTags(EditorName, true); 
      	}
      	function RxEphoxInsertPTagsB_]]><xsl:value-of select="$uniqueName"/><![CDATA[()
	   {
		var EditorName = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
		_insertPTags(EditorName, false); 
      	}
        function RxEphoxInsertPageBreak_]]><xsl:value-of select="$name"/><![CDATA[()
      	{
      		var EditorName = "]]><xsl:value-of select="@paramName" /><![CDATA["; 
			_insertPageBreak(EditorName, false);
      	}
      	]]>
      	/* Start of custom javascript functions */	
      	
      	&customEphoxFunctions;
      	
      	/* End of custom javascript functions */
        
         <![CDATA[
      	var currentLocale = "]]><xsl:value-of select="$lang"/><![CDATA[";
      	ePhox.setDebugLevel("]]><xsl:value-of select="$DebugLevel"/><![CDATA["); 
      	ePhox.setDownloadDirectory("]]><xsl:value-of select="$config_download"/><![CDATA[");
      	ePhox.setConfigurationFile("]]><xsl:value-of select="$config_app_url"/><![CDATA[");
      	ePhox.setStyles(escape("div.rx_ephox_inlinevariant {border: solid #c0c0c0 1px;}"));
      	ePhox.setMinimumJREVersion("1.4.2");
      	ePhox.setAutoSubmit(false);
      	ePhox.setLocale(currentLocale.toUpperCase().substr(0,2));
      	ePhox.setOnInitComplete("rxEphoxHandleEditorInitComplete");
      	 
	var ]]><xsl:value-of select="$uniqueName"/><![CDATA[_InlineLinkSlot = ]]><xsl:value-of select="$InlineLinkSlot"/><![CDATA[;
	var ]]><xsl:value-of select="$uniqueName"/><![CDATA[_InlineImageSlot = ]]><xsl:value-of select="$InlineImageSlot"/><![CDATA[;
	var ]]><xsl:value-of select="$uniqueName"/><![CDATA[_InlineVariantSlot = ]]><xsl:value-of select="$InlineVariantSlot"/><![CDATA[;
	var ]]><xsl:value-of select="$uniqueName"/><![CDATA[_ReadOnly = ']]><xsl:value-of select="@isReadOnly"/><![CDATA['; 
	
	ePhox.setDocument(document.forms[0].]]><xsl:value-of select="$uniqueName"/>_temp.value<![CDATA[); 
	ePhox.addJar("../sys_resources/ephox/rxEphoxExtensions.jar", "com.percussion.ephox.extensions.PSEphoxCustomEvents");
      
        ePhox.show();     
      
         // add a new editor object to the object array.  See rx_ephox.js for prototype. 
         var ed = new Object(); 
         ed.name = "]]><xsl:value-of select="$uniqueName"/><![CDATA["; 
         ed.objectref = ePhox; 
         ed.inlineLinkSlot = "]]><xsl:value-of select="$InlineLinkSlot"/><![CDATA["; 
         ed.inlineImageSlot = "]]><xsl:value-of select="$InlineImageSlot"/><![CDATA["; 
         ed.inlineVariantSlot = "]]><xsl:value-of select="$InlineVariantSlot"/><![CDATA["; 

         var ix = _rxAllEditors.length;
         _rxAllEditors[ix] = ed; 
      
   ]]></script>
	</xsl:template>	
	
	
</xsl:stylesheet>
