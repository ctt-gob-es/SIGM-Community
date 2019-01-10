<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:m="http://www.facturae.es/Facturae/2009/v3.2/Facturae"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:decimal-format grouping-separator="." decimal-separator=","/>

    <xsl:attribute-set name="prn-attrs">
        <xsl:attribute name="style">font-family:Courier;font-size:11pt;</xsl:attribute>
    </xsl:attribute-set>

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-width="21cm"
                                       page-height="29.7cm" margin-top="0.5cm"
                                       margin-bottom="0.5cm" margin-left="0.5cm" margin-right="0.5cm">
                    <fo:region-body margin="0.5cm"/>
                    <fo:region-before extent="0.5cm"/>
                    <fo:region-after extent="0.5cm"/>
                    <fo:region-start extent="0.5cm"/>
                    <fo:region-end extent="0.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates select="//m:Facturae"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <!--INICIO [dipucr-Felipe #1272]-->
    <xsl:template name="formatDate">
		<xsl:param name="date" />
		<xsl:variable name="year" select="substring-before($date, '-')" />
		<xsl:variable name="month" select="substring-before(substring-after($date, '-'), '-')" />
		<xsl:variable name="day" select="substring-after(substring-after($date, '-'), '-')" />
		<xsl:choose>
			<xsl:when test='$year!=""'>
				<xsl:value-of select="concat($day, '/', $month, '/', $year)" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!--FIN [dipucr-Felipe #1272]-->

    <xsl:template match="m:Facturae">

        <fo:table>
            <fo:table-column column-width="9.5cm"/>

            <fo:table-body>
                <fo:table-row text-align="right">
                    <fo:table-cell>
                        <fo:block font-size="12.0pt" font-family="sans-serif" font-weight="bold">
                            <xsl:choose>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceDocumentType="FC"'>
                                    Factura Completa
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceDocumentType="FA"'>
                                    Factura Abreviada
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceDocumentType="AF"'>
                                    Autofactura
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/InvoiceDocumentType"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            &#160; (
                            <xsl:choose>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="OO"'>
                                    Original
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="OR"'>
                                    Original Rectificativa
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="OC"'>
                                    Original Recapitulativa
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="CO"'>
                                    Copia Original
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="CR"'>
                                    Copia Rectificativa
                                </xsl:when>
                                <xsl:when test='/*/Invoices/Invoice/InvoiceHeader/InvoiceClass="CC"'>
                                    Copia Recapitulativa
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/InvoiceClass"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            )
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table>
            <fo:table-column column-width="9.5cm" border-width="0.4mm" border-style="solid"/>
            <fo:table-column column-width="9.5cm" border-width="0.4mm" border-style="solid"/>

            <fo:table-body>
                <fo:table-row border-width="0.4mm" border-style="solid">
                    <fo:table-cell height="3.5cm">
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Emisor (nombre, direccion...) / Seller (name, address, tax reference)
                        </fo:block>

                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            <xsl:choose>
                                <xsl:when test='/*/Parties/SellerParty/TaxIdentification/PersonTypeCode="F"'>
                                    Persona Física
                                </xsl:when>
                                <xsl:when test='/*/Parties/SellerParty/TaxIdentification/PersonTypeCode="J"'>
                                    Persona Jurídica
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="/*/Parties/SellerParty/TaxIdentification/PersonTypeCode"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:choose>
                                <xsl:when test="/*/Parties/SellerParty/TaxIdentification/PersonTypeCode = 'F'">
                                    <xsl:value-of
                                            select="/*/Parties/SellerParty/Individual/Name"/>&#160;
                                    <xsl:value-of
                                            select="/*/Parties/SellerParty/Individual/FirstSurname"/>&#160;
                                    <xsl:value-of
                                            select="/*/Parties/SellerParty/Individual/SecondSurname"/>
                                </xsl:when>
                                <xsl:when test="/*/Parties/SellerParty/TaxIdentification/PersonTypeCode = 'J'">
                                    <xsl:value-of select="/*/Parties/SellerParty/LegalEntity/CorporateName"/>
                                </xsl:when>
                            </xsl:choose>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/SellerParty/*/*/Address"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/SellerParty/*/*/PostCode"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/SellerParty/*/*/Town"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/SellerParty/*/*/Province"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/SellerParty/*/*/CountryCode"/>
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>

                        <fo:table>
                            <fo:table-column column-width="4.75cm" border-right-width="0.4mm"
                                             border-right-style="solid"/>
                            <fo:table-column column-width="4.75cm"/>

                            <fo:table-body>
                                <fo:table-row height="1cm">
                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Número de Factura / Invoice number
                                        </fo:block>

                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                            <xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/InvoiceSeriesCode"/>
                                            <xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/InvoiceNumber"/>
                                        </fo:block>

                                    </fo:table-cell>

                                    <fo:table-cell>
                                    		<fo:block>
                                    		<xsl:choose>
                               					<xsl:when test='/*/Invoices/Invoice/InvoiceHeader/Corrective/InvoiceNumber'>
	                                    		<fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                                            Factura rectificada
	                                        </fo:block>
	                                        
	                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
	                                        	<xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/Corrective/InvoiceSeriesCode"/>
	                                          <xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/Corrective/InvoiceNumber"/>
	                                          <fo:block font-size="7pt">
	                                          	<xsl:value-of select="/*/Invoices/Invoice/InvoiceHeader/Corrective/AdditionalReasonDescription"/>
	                                          </fo:block>
	                                        </fo:block>
	                                      </xsl:when>
			                                  </xsl:choose>
			                                  </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row height="1.5cm" border-top-style="solid" border-top-width="0.4mm">
                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Fecha de Expedicion de la Factura / Invoice date (yyyy-mm-dd)
                                        </fo:block>

                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                            <xsl:value-of select="/*/Invoices/Invoice/InvoiceIssueData/IssueDate"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            CIF/NIF del Emisor / Sellers reference
                                        </fo:block>

                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                            <xsl:value-of
                                                    select="/*/Parties/SellerParty/TaxIdentification/TaxIdentificationNumber"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row height="1.1cm" border-top-style="solid" border-top-width="0.4mm">
                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            CIF/NIF del Receptor / Buyer reference
                                        </fo:block>

                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                            <xsl:value-of
                                                    select="/*/Parties/BuyerParty/TaxIdentification/TaxIdentificationNumber"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Otro CIF/NIF / Other reference
                                        </fo:block>

                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                            </fo:table-body>
                        </fo:table>

                    </fo:table-cell>

                </fo:table-row>


                <fo:table-row border-width="0.4mm" border-style="solid" height="3.2cm">
                    <fo:table-cell>
                        <!-- INICIO [dipucr-Felipe #1363] -->
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Datos Adicionales de Factura / Invoice Additional Information
                        </fo:block>
                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
							<xsl:choose>
								<xsl:when
									test="/*/Invoices/Invoice/AdditionalData/InvoiceAdditionalInformation!=''">
									<xsl:value-of
										select="/*/Invoices/Invoice/AdditionalData/InvoiceAdditionalInformation" />
								</xsl:when>
							</xsl:choose>
						</fo:block>
						<!-- FIN [dipucr-Felipe #1363] -->
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Receptor (nombre, direccion...) / Buyer (name, address...)
                        </fo:block>

                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            <xsl:choose>
                                <xsl:when test='/*/Parties/BuyerParty/TaxIdentification/PersonTypeCode="F"'>
                                    Persona Física
                                </xsl:when>
                                <xsl:when test='/*/Parties/BuyerParty/TaxIdentification/PersonTypeCode="J"'>
                                    Persona Jurídica
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="/*/Parties/BuyerParty/TaxIdentification/PersonTypeCode"/>
                                </xsl:otherwise>
                            </xsl:choose>

                            &#160; / &#160;

                            <xsl:choose>
                                <xsl:when test='/*/Parties/BuyerParty/TaxIdentification/ResidenceTypeCode="E"'>
                                    Extrangero (fuera de la UE)
                                </xsl:when>
                                <xsl:when test='/*/Parties/BuyerParty/TaxIdentification/ResidenceTypeCode="R"'>
                                    Residente (en España)
                                </xsl:when>
                                <xsl:when test='/*/Parties/BuyerParty/TaxIdentification/ResidenceTypeCode="U"'>
                                    Residente en la Unión Europea (excepto España)
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="/*/Parties/BuyerParty/TaxIdentification/ResidenceTypeCode"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>


                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:choose>
                                <xsl:when test="/*/Parties/BuyerParty/TaxIdentification/PersonTypeCode = 'F'">
                                    <xsl:value-of
                                            select="/*/Parties/BuyerParty/Individual/Name"/>&#160;<xsl:value-of
                                        select="/*/Parties/BuyerParty/Individual/FirstSurname"
                                        />&#160;
                                    <xsl:value-of
                                            select="/*/Parties/BuyerParty/Individual/SecondSurname"/>

                                </xsl:when>
                                <xsl:when test="/*/Parties/BuyerParty/TaxIdentification/PersonTypeCode = 'J'">
                                    <xsl:value-of select="/*/Parties/BuyerParty/LegalEntity/CorporateName"/>
                                </xsl:when>
                            </xsl:choose>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/BuyerParty/*/*/Address"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/BuyerParty/*/*/PostCode"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/BuyerParty/*/*/Town"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/BuyerParty/*/*/Province"/>
                        </fo:block>

                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                            <xsl:value-of select="/*/Parties/BuyerParty/*/*/CountryCode"/>
                        </fo:block>

                    </fo:table-cell>
                </fo:table-row>


                <fo:table-row>
                    <fo:table-cell>
                        <fo:table>
                            <fo:table-column column-width="9.5cm"/>

                            <fo:table-body>
                            		
                                <fo:table-row height="1.65cm">
                                		<fo:table-cell>
                                    		<fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Cuenta de Abono / Account to be credited
                                        </fo:block>
                                        <xsl:for-each select="//*/AccountToBeCredited">
		                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
		                                            <xsl:value-of select="IBAN"/>
		                                        </fo:block>
		                                        <xsl:for-each select="./BranchInSpainAddress">
				                                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
				                                            <xsl:value-of select="Address"/>,
				                                            <xsl:value-of select="PostCode"/>
				                                            (<xsl:value-of select="Town"/>)
				                                        </fo:block>
				                                    </xsl:for-each>
				                                </xsl:for-each>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row height="1.65cm">
                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Cuenta de Cargo / Account to be debited
                                        </fo:block>
                                        <xsl:for-each select="//*/AccountToBeDebited">
		                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
		                                            <xsl:value-of select="IBAN"/>
		                                        </fo:block>
		                                        <xsl:for-each select="./BranchInSpainAddress">
				                                        <fo:block font-family="Courier" font-size="9pt" text-align="left">
				                                            <xsl:value-of select="Address"/>,
				                                            <xsl:value-of select="PostCode"/>
				                                            (<xsl:value-of select="Town"/>)
				                                        </fo:block>
				                                    </xsl:for-each>
				                                </xsl:for-each>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="5cm" border-right-style="solid"
                                                             border-right-width="0.4mm"/>
                                            <fo:table-column column-width="4.5cm"/>

                                            <fo:table-body>
                                                <fo:table-row height="1cm" border-top-style="solid"
                                                              border-top-width="0.4mm">
                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Medio de pago preferente / Payment means
                                                        </fo:block>
                                                        <xsl:for-each select="//*/PaymentDetails/Installment">
		                                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
										                                            <xsl:choose>
		                                   															<xsl:when test='PaymentMeans!=""'>
		                                        														<xsl:choose>
																                                            <xsl:when test='PaymentMeans="01"'>
																                                                Al contado
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="02"'>
																                                                Recibo Domiciliado
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="03"'>
																                                                Recibo
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="04"'>
																                                                Transferencia
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="05"'>
																                                                Letra Aceptada
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="06"'>
																                                                Crédito Documentario
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="07"'>
																                                                Contrato Adjudicación
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="08"'>
																                                                Letra de Cambio
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="09"'>
																                                                Pagaré a la Orden
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="10"'>
																                                                Pagaré No a la Orden
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="11"'>
																                                                Cheque
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="12"'>
																                                                Reposición
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="13"'>
																                                                Especiales
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="14"'>
																                                                Compensación
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="15"'>
																                                                Giro postal
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="16"'>
																                                                Cheque conformado
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="17"'>
																                                                Cheque bancario
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="18"'>
																                                                Pago contra reembolso
																                                            </xsl:when>
																                                            <xsl:when test='PaymentMeans="19"'>
																                                                Pago mediante tarjeta
																                                            </xsl:when>
																                                        </xsl:choose>
																                                     </xsl:when> 	
																                                </xsl:choose>       	
										                                        </fo:block>
                                                        </xsl:for-each>>
                                                    </fo:table-cell>

                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Fecha de Vencimiento / Due Date
                                                        </fo:block>
                                                        <fo:block font-family="Courier" font-size="11pt"
                                                                  text-align="left">
                                                            <xsl:call-template name="formatDate">
                                                            		<xsl:with-param name="date" select="//*/Installment/InstallmentDueDate" />
                                                            </xsl:call-template>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row height="1cm" border-top-style="solid"
                                                              border-top-width="0.4mm">
                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Periodo de facturación
                                                        </fo:block>
                                                        <!--[dipucr-Felipe #1272]-->
                                                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
                                                            <xsl:choose>
                                                            <xsl:when test='//*/InvoicingPeriod/StartDate!=""'>
                                                            	<xsl:call-template name="formatDate">
                                                            		<xsl:with-param name="date" select="//*/InvoicingPeriod/StartDate" />
                                                            	</xsl:call-template>-<xsl:call-template name="formatDate">
                                                            		<xsl:with-param name="date" select="//*/InvoicingPeriod/EndDate" />
                                                            	</xsl:call-template>
                                                            </xsl:when>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Lugar final de la entrega / Comprador
                                                        </fo:block>
                                                        <!-- INICIO [dipucr-Felipe #1272] -->
                                                        <fo:block font-family="Courier" font-size="11pt" text-align="left">
                                                            <xsl:for-each select="//*/AdministrativeCentre">
                                                            	<xsl:choose>
								                                		<xsl:when test='./RoleTypeCode="04"'>
								                                				<xsl:value-of select="./Name"/>
								                                				
								                                				<xsl:if test='./Name=""'>
								                                					<xsl:value-of select="./CentreDescription"/>
								                                				</xsl:if>
								                                		</xsl:when>
								                                </xsl:choose>
                                                            </xsl:for-each>
                                                        </fo:block>
                                                        <!-- FIN [dipucr-Felipe #1272] -->
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                </fo:table-row>

                            </fo:table-body>
                        </fo:table>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:table>
                            <fo:table-column column-width="9.5cm"/>

                            <fo:table-body>
                                <fo:table-row height="1.1cm">
                                    <fo:table-cell>
                                    	<!-- INICIO [dipucr-Felipe #1363] -->
                                        <fo:table>
                                            <fo:table-column column-width="4.75cm" border-right-style="solid"
                                                             border-right-width="0.4mm"/>
                                            <fo:table-column column-width="4.75cm"/>

                                            <fo:table-body>
                                                <fo:table-row height="1.1cm">
                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Nº Contrato Emisor / Issuer Contract
                                                        </fo:block>
                                                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
                                                            <xsl:choose>
							                                	<xsl:when test='/*/Invoices/Invoice/IssuerContractReference!=""'>
							                                		<xsl:value-of select="/*/Invoices/Invoice/IssuerContractReference"/>
							                                	</xsl:when>
							                                </xsl:choose>
							                            </fo:block>
							                            <fo:block font-family="Courier" font-size="10pt" text-align="left">
							                                <xsl:choose>
							                                	<xsl:when test='/*/Invoices/Invoice/Items/InvoiceLine/IssuerContractReference!=""'>
							                                		<xsl:value-of select="/*/Invoices/Invoice/Items/InvoiceLine/IssuerContractReference"/>
							                                	</xsl:when>
							                                </xsl:choose>
							                            </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Nº Contrato Receptor / Receiver Contract
                                                        </fo:block>
                                                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
                                                            <xsl:choose>
							                                	<xsl:when test='/*/Invoices/Invoice/ReceiverContractReference!=""'>
							                                		<xsl:value-of select="/*/Invoices/Invoice/ReceiverContractReference"/>
							                                	</xsl:when>
							                                </xsl:choose>
							                            </fo:block>
							                            <fo:block font-family="Courier" font-size="10pt" text-align="left">
							                                <xsl:choose>
							                                	<xsl:when test='/*/Invoices/Invoice/Items/InvoiceLine/ReceiverContractReference!=""'>
							                                		<xsl:value-of select="/*/Invoices/Invoice/Items/InvoiceLine/ReceiverContractReference"/>
							                                	</xsl:when>
							                                </xsl:choose>
							                            </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                         </fo:table>
                                         <!-- FIN [dipucr-Felipe #1363] -->
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row border-top-style="solid" border-top-width="0.4mm">
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="4.75cm" border-right-style="solid"
                                                             border-right-width="0.4mm"/>
                                            <fo:table-column column-width="4.75cm"/>

                                            <fo:table-body>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:table>
                                                            <fo:table-column column-width="2.375cm"/>
                                                            <fo:table-column column-width="2.375cm"/>

                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                                  text-align="left">
                                                                            Pais de Origen / Country of origin
                                                                        </fo:block>

                                                                        <fo:block font-family="Courier" font-size="11pt"
                                                                                  text-align="left">
                                                                            <xsl:value-of
                                                                                    select="/*/Parties/SellerParty/*/*/CountryCode"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>

                                                                    <fo:table-cell>
                                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                                  text-align="left">
                                                                            Codigo ISO / ISO code
                                                                        </fo:block>
                                                                    </fo:table-cell>

                                                                </fo:table-row>

                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:table-cell>

                                                    <fo:table-cell>
                                                        <fo:table>
                                                            <fo:table-column column-width="2.375cm"/>
                                                            <fo:table-column column-width="2.375cm"/>

                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                                  text-align="left">
                                                                            Pais de Destino / Country of dest.
                                                                        </fo:block>
                                                                        <fo:block font-family="Courier" font-size="11pt"
                                                                                  text-align="left">
                                                                            <xsl:value-of
                                                                                    select="/*/Parties/BuyerParty/*/*/CountryCode"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>

                                                                    <fo:table-cell>
                                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                                  text-align="left">
                                                                            Codigo ISO / ISO code
                                                                        </fo:block>
                                                                    </fo:table-cell>

                                                                </fo:table-row>

                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:table-cell>

                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row border-top-style="solid" border-top-width="0.4mm">
                                    <fo:table-cell>
                                        <fo:table>
                                            <fo:table-column column-width="4.75cm"/>
                                            <fo:table-column column-width="4.75cm"/>

                                            <fo:table-body>
                                                <fo:table-row height="1.1cm">
                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Plazos de pago / Terms of payment
                                                        </fo:block>
                                                    </fo:table-cell>

                                                    <fo:table-cell>
                                                        <fo:block font-size="7.0pt" font-family="sans-serif"
                                                                  text-align="left">
                                                            Divisa de pago / Transaction currency
                                                        </fo:block>
                                                        <fo:block font-family="Courier" font-size="11pt"
                                                                  text-align="left">
                                                            <xsl:value-of select="//*/Batch/InvoiceCurrencyCode"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                </fo:table-row>
                                
                                <!-- INICIO [dipucr-Felipe #1363] -->
                                <fo:table-row border-top-style="solid" border-top-width="0.4mm">
                                    <fo:table-cell>
                                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                                            Observaciones de cobro / Collection additional information
                                        </fo:block>
                                        <fo:block font-family="Courier" font-size="10pt" text-align="left">
                                            <xsl:value-of select="/*/Invoices/Invoice/PaymentDetails/Installment/CollectionAdditionalInformation"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <!-- FIN [dipucr-Felipe #1363] -->

                            </fo:table-body>
                        </fo:table>
                    </fo:table-cell>
                </fo:table-row>

            </fo:table-body>
        </fo:table>

        <fo:table>
            <!--<fo:table-column column-width="2cm" border-style="solid" border-width="0.4mm"/>-->
            <fo:table-column column-width="5.02cm" border-style="solid" border-width="0.4mm"/><!--[dipucr-Felipe #1257] Sumamos 2cm-->
            <fo:table-column column-width="2.24cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="2.24cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>
            <fo:table-column column-width="1.583cm" border-style="solid" border-width="0.4mm"/>


            <fo:table-body>
                <fo:table-row>
                    <!-- [dipucr-Felipe #1257]
                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Codigo Detalle / Item/packages
                        </fo:block>
                    </fo:table-cell>-->

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Descripcion Detalle / Item description
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Cantidad / Quantity
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Precio Unitario / Unit price
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Porcentaje del Descuento / Discount percentage
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Total sin impuestos / Amount (gross)
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Tipo de Impuesto / Tax
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Porcentaje del Impuesto / Tax percentage
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Fecha de Devengo / Date of Accrued income
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Total con impuestos / Amount Due
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>

                <xsl:for-each select="//*/InvoiceLine">
                    <fo:table-row>
                    		<!-- INICIO [dipucr-Felipe #1257] -->
                        <!--<fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="left">
                            </fo:block>
                        </fo:table-cell>-->

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="left">
                                <xsl:value-of select="./ItemDescription"/>
                                <xsl:choose>
                                	<xsl:when test='./AdditionalLineItemInformation!=""'>
                               			<xsl:text> - </xsl:text>
                               			<xsl:value-of select="./AdditionalLineItemInformation"/>
                               		</xsl:when>
                                </xsl:choose>
                                <!--[dipucr-Felipe #1272]-->	                                		
                                <xsl:choose>
                                		<xsl:when test='./ReceiverContractReference!=""'>
                                				<xsl:text> - Nº Contrato </xsl:text>
                                				<xsl:value-of select="./ReceiverContractReference"/>
                                		</xsl:when>
                                </xsl:choose>	
                            </fo:block>
                        </fo:table-cell>
                    		<!-- FIN [dipucr-Felipe #1257] -->

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                                <xsl:value-of select="format-number(./Quantity,'#.##0,0###')"/>
                                <xsl:choose>
                                    <xsl:when test='UnitOfMeasure!=""'>
                                        <xsl:choose>
                                            <xsl:when test='UnitOfMeasure="01"'>
                                                Unidades
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="02"'>
                                                Horas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="03"'>
                                                Kilogramos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="04"'>
                                                Litros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="05"'>
                                                Otros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="06"'>
                                                Cajas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="07"'>
                                                Bandejas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="08"'>
                                                Barriles
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="09"'>
                                                Bidones
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="10"'>
                                                Bolsas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="11"'>
                                                Bombonas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="12"'>
                                                Botellas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="13"'>
                                                Botes
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="14"'>
                                                Tetra Bricks
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="15"'>
                                                Centilitros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="16"'>
                                                Centímetros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="17"'>
                                                Cubos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="18"'>
                                                Docenas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="19"'>
                                                Estuches
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="20"'>
                                                Garrafas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="21"'>
                                                Gramos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="22"'>
                                                Kilómetros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="23"'>
                                                Latas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="24"'>
                                                Manojos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="25"'>
                                                Metros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="26"'>
                                                Milímetros
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="27"'>
                                                6-Packs
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="28"'>
                                                Paquetes
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="29"'>
                                                Raciones
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="30"'>
                                                Rollos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="31"'>
                                                Sobres
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="32"'>
                                                Tarrinas
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="33"'>
                                                Metros cúbicos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="34"'>
                                                Segundos
                                            </xsl:when>
                                            <xsl:when test='UnitOfMeasure="35"'>
                                                Vatios
                                            </xsl:when>
                                            <xsl:otherwise>
                                                -
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        -
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                                <xsl:value-of select="format-number(./UnitPriceWithoutTax,'#.##0,00####')"/>
                            </fo:block>
                        </fo:table-cell>

												
                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                            		<!-- [dipucr-Felipe #1272] Condición para que no salga NaN-->
																<xsl:choose>
				               						<xsl:when test='./DiscountsAndRebates/Discount/DiscountRate!=""' >
				                             <xsl:value-of 
				                             		select="format-number(./DiscountsAndRebates/Discount/DiscountRate,'#.##0,00##')"/>
				                          </xsl:when>
				                        </xsl:choose>
                            </fo:block>
                        </fo:table-cell>
		                    

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                                <xsl:choose>
                                    <xsl:when test="FileHeader/Batch/InvoiceCurrencyCode='EUR'">
                                        <xsl:variable name="taxableBase">
                                            <xsl:value-of select="./TaxesOutputs/Tax/TaxableBase"/>
                                        </xsl:variable>
                                        <xsl:value-of
                                                select="format-number(./TaxesOutputs/Tax/TaxableBase,'#.##0,00')"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:variable name="totalAmount">
                                            <xsl:value-of select="./TaxesOutputs/Tax/TaxableBase/TotalAmount"/>
                                        </xsl:variable>
                                        <xsl:value-of
                                                select="format-number(./TaxesOutputs/Tax/TaxableBase/TotalAmount,'#.##0,00')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="center">
                                <xsl:choose>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="01"'>
                                        IVA
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="02"'>
                                        IPSI
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="03"'>
                                        IGIC
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="04"'>
                                        IRPF
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="05"'>
                                        OTRO
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="06"'>
                                        ITPAJD
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="07"'>
                                        IE
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="08"'>
                                        Ra
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="09"'>
                                        ICTECM
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="10"'>
                                        IECDPCAC
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="11"'>
                                        IIIMAB
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="12"'>
                                        ICIO
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="13"'>
                                        IMVDM
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="14"'>
                                        IMSM
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="15"'>
                                        IMGSM
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="16"'>
                                        IMPN
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="17"'>
                                        REIVA
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="18"'>
                                        REIGIC
                                    </xsl:when>
                                    <xsl:when test='./TaxesOutputs/Tax/TaxTypeCode="19"'>
                                        REIPSI
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="./TaxesOutputs/Tax/TaxTypeCode"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                                <xsl:value-of select="format-number(./TaxesOutputs/Tax/TaxRate,'#.##0,00')"/>
                            </fo:block>
                        </fo:table-cell>

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="center">
                                <xsl:value-of select="./TransactionDate"/>
                            </fo:block>
                        </fo:table-cell>

                        <fo:table-cell>
                            <fo:block font-family="Courier" font-size="0.6em" text-align="right">
                                <xsl:choose>
                                    <xsl:when test="FileHeader/Batch/InvoiceCurrencyCode='EUR'">
                                        <xsl:variable name="a" select="./TaxesOutputs/Tax/TaxAmount"/>
                                        <xsl:variable name="b" select="./TaxesOutputs/Tax/TaxableBase"/>
                                        <xsl:variable name="total"
                                                      select="$a + $b"/>
                                        <xsl:value-of select="format-number($total,'#.##0,00')"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:variable name="a" select="./TaxesOutputs/Tax/TaxAmount/TotalAmount"/>
                                        <xsl:variable name="b" select="./TaxesOutputs/Tax/TaxableBase/TotalAmount"/>
                                        <xsl:variable name="total"
                                                      select="$a + $b"/>
                                        <xsl:value-of select="format-number($total,'#.##0,00')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>

            </fo:table-body>
        </fo:table>


        <fo:table>
            <fo:table-column column-width="15.84cm"/>
            <fo:table-column column-width="3.16cm" border-style="solid" border-width="0.4mm"/>


            <fo:table-body>
            
            	<!-- INICIO [dipucr-Felipe #1363] Descuentos sobre el total importe bruto -->
                <xsl:choose>
                <xsl:when test='/*/Invoices/Invoice/InvoiceTotals/TotalGeneralDiscounts!=0' >
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Total Importe Bruto / Total gross amount
	                        </fo:block>
	                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
	                            <xsl:value-of
	                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalGrossAmount, '#.##0,00')"/>
	                        </fo:block>
	                    </fo:table-cell>
	                </fo:table-row>
	                
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Total Descuentos Generales / Total General Discounts
	                        </fo:block>
	                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
	                            <xsl:value-of
	                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalGeneralDiscounts,'#.##0,00')"/>
	                        </fo:block>
	                    </fo:table-cell>
	                </fo:table-row>
                </xsl:when>
                </xsl:choose>
                <!-- FIN [dipucr-Felipe #1363] -->
            
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Base imponible / Total gross amount (before taxes)
                        </fo:block>
                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
                            <xsl:value-of
                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalGrossAmountBeforeTaxes,'#.##0,00')"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                
                <!-- INICIO [dipucr-Felipe #1260] IVA Total -->
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Total Impuestos / Total Tax Outputs
                        </fo:block>
                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
                            <xsl:value-of
                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalTaxOutputs,'#.##0,00')"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- FIN [dipucr-Felipe #1260] -->
                
                <!-- INICIO [dipucr-Felipe #1272] Impuestos retenidos -->
                <xsl:choose>
                <xsl:when test='/*/Invoices/Invoice/InvoiceTotals/TotalTaxesWithheld!=0' >
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Impuestos retenidos / Total Taxes Withheld
	                        </fo:block>
	                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
	                            <xsl:value-of
	                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalTaxesWithheld,'#.##0,00')"/>
	                        </fo:block>
	                    </fo:table-cell>
	                </fo:table-row>
                </xsl:when>
                </xsl:choose>
                <!-- FIN [dipucr-Felipe #1272] -->
                
                <!-- INICIO [dipucr-Felipe #1298] -->
                <!-- Retenciones globales a nivel de factura -->
                <xsl:choose>
                <xsl:when test='/*/Invoices/Invoice/InvoiceTotals/AmountsWithheld/WithholdingAmount!=0' >
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Retenciones globales / Wihtholding amount
	                        </fo:block>
	                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
	                            <xsl:value-of
	                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/AmountsWithheld/WithholdingAmount,'#.##0,00')"/>
	                        </fo:block>
	                    </fo:table-cell>
	                </fo:table-row>
                </xsl:when>
                </xsl:choose>
                
                <!-- Suplidos -->
                <xsl:choose>
                <xsl:when test='/*/Invoices/Invoice/InvoiceTotals/TotalReimbursableExpenses!=0' >
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Suplidos / Total Reimbursable Expenses
	                        </fo:block>
	                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
	                            <xsl:value-of
	                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalReimbursableExpenses,'#.##0,00')"/>
	                        </fo:block>
	                    </fo:table-cell>
	                </fo:table-row>
                </xsl:when>
                </xsl:choose>
                <!-- FIN [dipucr-Felipe #1298] -->
                
                <!-- INICIO [dipucr-Felipe #1363bis 03.11.15] -->
                <!-- Anticipos de pago -->
                <xsl:if test='/*/Invoices/Invoice/InvoiceTotals/PaymentsOnAccount' >
	                <fo:table-row>
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                        </fo:block>
	                    </fo:table-cell>
	
	                    <fo:table-cell>
	                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
	                            Anticipos de pago / Payments On Account
	                        </fo:block>
	                        <xsl:for-each select="//*/PaymentOnAccount">
		                        <fo:block font-family="Courier" font-size="8pt" text-align="right">
		                        	Fecha:
		                        	<xsl:call-template name="formatDate">
		                        		<xsl:with-param name="date" select="./PaymentOnAccountDate" />
		                        	</xsl:call-template>
		                        </fo:block>
		                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
		                            <xsl:value-of select="./PaymentOnAccountAmount"/>
		                        </fo:block>
                			</xsl:for-each>
	                    </fo:table-cell>
	                </fo:table-row>
                </xsl:if>
                <!-- FIN [dipucr-Felipe #1363bis] -->

                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                        </fo:block>
                    </fo:table-cell>

                    <fo:table-cell border-top-style="solid" border-top-width="0.4mm">
                        <fo:block font-size="7.0pt" font-family="sans-serif" text-align="left">
                            Total a Ejecutar / Total Executable Amount
                        </fo:block>
                        <fo:block font-family="Courier" font-size="11pt" text-align="right">
                            <xsl:value-of
                                    select="format-number(/*/Invoices/Invoice/InvoiceTotals/TotalExecutableAmount,'#.##0,00')"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>

            </fo:table-body>
        </fo:table>

    </xsl:template>


</xsl:stylesheet>
