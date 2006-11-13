<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

     <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    
     <xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
"http://www.springframework.org/dtd/spring-beans.dtd"&gt;</xsl:text>
        	<xsl:apply-templates select="*" mode="start"/>
     </xsl:template>
     
	<xsl:template match="node()" mode="start">
		<xsl:choose>
		<xsl:when test="name() = 'bean'">
			<xsl:copy>
				<xsl:apply-templates select="@*" mode="att"/>
				<xsl:apply-templates select="node()" mode="start"/>
			</xsl:copy>
			<xsl:if test="not(//bean[@id='paginatorContentListServlet'])">
				<bean id="paginatorContentListServlet"
					class="org.springframework.web.servlet.mvc.ServletWrappingController">
					<property name="servletClass">
						<value>
							com.percussion.pso.scripps.paginator.ContentListServlet
						</value>
					</property>
					<property name="initParameters">
						<props>
							<prop key="debug">true</prop>
							<prop key="httpParamPageNo">pageno</prop>
						</props>
					</property>
				</bean>
			</xsl:if>
		</xsl:when>
		<xsl:otherwise>
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="name() = 'props'">
					<xsl:apply-templates select="node()" mode="start"/>
					<xsl:if test="not(//prop[@key='paginator/contentlist.htm'])">
						<prop key="paginator/contentlist.htm">paginatorContentListServlet</prop>
					</xsl:if>
				</xsl:when>
				<xsl:when test="name() = 'bean'">
				
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="@*" mode="att"/>
					<xsl:apply-templates select="node()" mode="start"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
	</xsl:otherwise>
	</xsl:choose>
     </xsl:template>
     
     <xsl:template match="@*" mode="att">
		<xsl:copy>
			<xsl:apply-templates select="@*" mode="att"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
