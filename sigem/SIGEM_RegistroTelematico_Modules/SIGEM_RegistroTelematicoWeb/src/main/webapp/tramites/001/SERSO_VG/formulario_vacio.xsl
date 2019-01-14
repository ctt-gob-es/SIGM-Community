<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="templates.xsl" />


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'CAMPAÑA SOBRE LA ERRADICACIÓN DE LA VIOLENCIA DE GÉNERO E INTERCULTURALIDAD'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo*'"/>

	<xsl:variable name="lang.convocatoria" select="'Convocatorias abiertas:'"/>
	<xsl:variable name="lang.seleccionar" select="'Seleccionar *'"/>
	<xsl:variable name="lang.convocatoriaObligatoria" select="'Convocatorias'"/>

	<xsl:variable name="lang.ayto" select="'Ayuntamiento/EATIM'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Persona de contacto responsable de la actividad en el ayuntamiento / EATIM'"/>
	<xsl:variable name="lang.datosRepresentante2" select="'  (Servicios Sociales o Centro Mujer)'"/>
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Nombre y Apellidos'"/>
	<xsl:variable name="lang.cargo_repre" select="'Cargo'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Región / País'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.centros" select="'Centros Solicitados'"/>

	<xsl:variable name="lang.centroX" select="' Centro Escolar '"/>

	<xsl:variable name="lang.nombreCentro" select="'Nombre del Centro Escolar'"/>
	<xsl:variable name="lang.direccionCentro" select="'Dirección'"/>
	<xsl:variable name="lang.telefonoCentro" select="'Teléfono'"/>
	<xsl:variable name="lang.faxCentro" select="'Fax'"/>
	<xsl:variable name="lang.emailCentro" select="'Correo Electrónico'"/>
	<xsl:variable name="lang.contactoCentro" select="'Nombre del/a responsable/s o persona/s de contacto del colegio o I.E.S.'"/>
	<xsl:variable name="lang.cargoCentro" select="'Cargo'"/>

	<!-- Se añaden los campos del Bullying -->
	<xsl:variable name="lang.tutor" select="'Tutor/a'"/>
	<xsl:variable name="lang.director" select="'Director/a'"/>
	<xsl:variable name="lang.secretario" select="'Secretario/a'"/>
	<xsl:variable name="lang.jefeEstudios" select="'Jefe/a de Estudios'"/>

	<xsl:variable name="lang.tipoCentro" select="'El centro es'"/>
	<xsl:variable name="lang.publico" select="'Público'"/>
	<xsl:variable name="lang.concertado" select="'Concertado'"/>
	<xsl:variable name="lang.privado" select="'Privado'"/>

	<xsl:variable name="lang.actividadesSolicitadas" select="'Actividades Solicitadas del Centro'"/>

	<xsl:variable name="lang.talleresIgualdad" select="'TALLERES DE EDUCACIÓN EN LA IGUALDAD'"/>
	<xsl:variable name="lang.numGrupos" select="'Nº Total de Grupos de 1º de la E.S.O.'"/>
	<xsl:variable name="lang.numAlunmos" select="'Nº de Alumnos/as de cada grupo de 1º de la E.S.O.'"/>
	<xsl:variable name="lang.a" select="'A '"/>
	<xsl:variable name="lang.b" select="'B '"/>
	<xsl:variable name="lang.c" select="'C '"/>
	<xsl:variable name="lang.d" select="'D '"/>
	<xsl:variable name="lang.e" select="'E '"/>
	<xsl:variable name="lang.numTotalIgualdad" select="'Nº Total de Talleres solicitados en Igualdad'"/>

	<xsl:variable name="lang.talleresIntercultura" select="'TALLERES DE EDUCACIÓN EN INTERCULTURALIDAD'"/>
	<xsl:variable name="lang.numCursos4" select="'Nº de cursos de 4º '"/>
	<xsl:variable name="lang.numCursos5" select="'Nº de cursos de 5º '"/>
	<xsl:variable name="lang.numCursos6" select="'Nº de cursos de 6º '"/>
	<xsl:variable name="lang.especificar" select="'especificar nº de alumnos/as por grupo'"/>
	<xsl:variable name="lang.numTotalIntercultura" select="'Nº Total de Talleres solicitados en Interculturalidad'"/>

	<!-- Se añaden los campos del Bullying -->
	<xsl:variable name="lang.talleresBullying" select="'TALLERES CONTRA BULLYING Y CIBERBULLYING'"/>
	<xsl:variable name="lang.numGruposBullying" select="'Nº Total de Grupos de 2º de la E.S.O.'"/>
	<xsl:variable name="lang.numAlunmosBullying" select="'Nº de Alumnos/as de cada grupo de 2º de la E.S.O.'"/>
	<xsl:variable name="lang.numTotalBullying" select="'Nº Total de Talleres solicitados Contra el Bullying y Ciberbullying'"/>

	<xsl:variable name="lang.añadir_centro" select="'[Añadir Centro]'"/>
	<xsl:variable name="lang.maximo" select="' (Máximo 8)'"/>

	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formula el solicitante'"/>
	
	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>
	
	<xsl:variable name="lang.declaro1" select="'Que al día de la fecha, esta entidad se encuentra al corriente en el cumplimiento de sus obligaciones tributarias y frente a la Seguidad Social, así como de sus obligaciones fiscales con la Excma. Diputación Provincial de Ciudad Real, no es deudora por resolución de procedencia de reintegro de subvenciones, y autorizo a la Diputación Provincial para la obtención de los certificados de la Agencia Estatal de la Administración Tributaria y de la Tesorería Territorial de la Seguridad de estar al corriente en el cumplimiento de dichas obligaciones.'"/>
	<xsl:variable name="lang.declaro2" select="'Que no me encuentro/esta entidad no se encuentra incursa en ninguna de las circunstancias de prohibición para la obtención de la condición de beneficiario de ayuda o subvención, previstas en el artículo 13 de la Ley General de Subvenciones, de 17 de noviembre de 2003.'"/>
	<xsl:variable name="lang.declaro3" select="'Que la persona o entidad solicitante autoriza expresamente a la Diputación de Ciudad Real para consultar las expresadas circunstancias ante las entidades señaladas.'"/>	

	<xsl:variable name="lang.anexar" select="'DOCUMENTACIÓN QUE SE ACOMPAÑA'"/>

	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexarse la documentación establecida en la convocatoria.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento está en soporte papel, debe escanearlo con carácter previo. Los originales quedan a disposición de la Diputación Provincial durante toda la tramitación del expediente.'"/>

	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipoXSIG" select="'Archivo XSIG'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
	
		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(4);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
						
			validar[2] = new Array('convocatoria','<xsl:value-of select="$lang.convocatoriaObligatoria"/>');

			validar[3] = new Array('ayuntamiento','<xsl:value-of select="$lang.ayto"/>');

			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(391);
			
			especificos[0] = new Array('cargo','cargo');

			especificos[1] = new Array('ayuntamiento','ayuntamiento');

			especificos[2] = new Array('nif_repre','nif_repre');
			especificos[3] = new Array('nombre_repre','nombre_repre');
			especificos[4] = new Array('cargo_repre','cargo_repre');
			especificos[5] = new Array('domicilio_repre','domicilio_repre');
			especificos[6] = new Array('ciudad_repre','ciudad_repre');
			especificos[7] = new Array('c_postal_repre','c_postal_repre');
			especificos[8] = new Array('region_repre','region_repre');
			especificos[9] = new Array('movil_repre','movil_repre');
			especificos[10] = new Array('d_email_repre','d_email_repre');

			//Primer centro escolar

			especificos[11] = new Array('nombreCentro1','nombreCentro1');
			especificos[12] = new Array('direccionCentro1','direccionCentro1');
			especificos[13] = new Array('telefonoCentro1','telefonoCentro1');
			especificos[14] = new Array('faxCentro1','faxCentro1');
			especificos[15] = new Array('emailCentro1','emailCentro1');
			especificos[16] = new Array('contactoCentro1','contactoCentro1');
			especificos[17] = new Array('cargoCentro1','cargoCentro1');

			especificos[18] = new Array('publico1','publico1');
			especificos[19] = new Array('concertado1','concertado1');
			especificos[20] = new Array('privado1','privado1');

			especificos[21] = new Array('numGrupos1','numGrupos1');
			especificos[22] = new Array('a1','a1');
			especificos[23] = new Array('b1','b1');
			especificos[24] = new Array('c1','c1');
			especificos[25] = new Array('d1','d1');
			especificos[26] = new Array('e1','e1');
			especificos[27] = new Array('numTotalIgualdad1','numTotalIgualdad1');
			especificos[28] = new Array('numCursos41','numCursos41');
			especificos[29] = new Array('a41','a41');
			especificos[30] = new Array('b41','b41');
			especificos[31] = new Array('c41','c41');
			especificos[32] = new Array('d41','d41');
			especificos[33] = new Array('e41','e41');
			especificos[34] = new Array('numCursos51','numCursos51');
			especificos[35] = new Array('a51','a51');
			especificos[36] = new Array('b51','b51');
			especificos[37] = new Array('c51','c51');
			especificos[38] = new Array('d51','d51');
			especificos[39] = new Array('e51','e51');
			especificos[40] = new Array('numCursos61','numCursos61');
			especificos[41] = new Array('a61','a61');
			especificos[42] = new Array('b61','b61');
			especificos[43] = new Array('c61','c61');
			especificos[44] = new Array('d61','d61');
			especificos[45] = new Array('e61','e61');
			especificos[46] = new Array('numTotalIntercultura1','numTotalIntercultura1');

			//Segundo centro escolar

			especificos[47] = new Array('nombreCentro2','nombreCentro2');
			especificos[48] = new Array('direccionCentro2','direccionCentro2');
			especificos[49] = new Array('telefonoCentro2','telefonoCentro2');
			especificos[50] = new Array('faxCentro2','faxCentro2');
			especificos[51] = new Array('emailCentro2','emailCentro2');
			especificos[52] = new Array('contactoCentro2','contactoCentro2');
			especificos[53] = new Array('cargoCentro2','cargoCentro2');

			especificos[54] = new Array('publico2','publico2');
			especificos[55] = new Array('concertado2','concertado2');
			especificos[56] = new Array('privado2','privado2');
			
			especificos[57] = new Array('numGrupos2','numGrupos2');
			especificos[58] = new Array('a2','a2');
			especificos[59] = new Array('b2','b2');
			especificos[60] = new Array('c2','c2');
			especificos[61] = new Array('d2','d2');
			especificos[62] = new Array('e2','e2');
			especificos[63] = new Array('numTotalIgualdad2','numTotalIgualdad2');
			especificos[64] = new Array('numCursos42','numCursos42');
			especificos[65] = new Array('a42','a42');
			especificos[66] = new Array('b42','b42');
			especificos[67] = new Array('c42','c42');
			especificos[68] = new Array('d42','d42');
			especificos[69] = new Array('e42','e42');
			especificos[70] = new Array('numCursos52','numCursos52');
			especificos[71] = new Array('a52','a52');
			especificos[72] = new Array('b52','b52');
			especificos[73] = new Array('c52','c52');
			especificos[74] = new Array('d52','d52');
			especificos[75] = new Array('e52','e52');
			especificos[76] = new Array('numCursos62','numCursos62');
			especificos[77] = new Array('a62','a62');
			especificos[78] = new Array('b62','b62');
			especificos[79] = new Array('c62','c62');
			especificos[80] = new Array('d62','d62');
			especificos[81] = new Array('e62','e62');
			especificos[82] = new Array('numTotalIntercultura2','numTotalIntercultura2');

			//Tercer centro escolar

			especificos[83] = new Array('nombreCentro3','nombreCentro3');
			especificos[84] = new Array('direccionCentro3','direccionCentro3');
			especificos[85] = new Array('telefonoCentro3','telefonoCentro3');
			especificos[86] = new Array('faxCentro3','faxCentro3');
			especificos[87] = new Array('emailCentro3','emailCentro3');
			especificos[88] = new Array('contactoCentro3','contactoCentro3');
			especificos[89] = new Array('cargoCentro3','cargoCentro3');

			especificos[90] = new Array('publico3','publico3');
			especificos[91] = new Array('concertado3','concertado3');
			especificos[92] = new Array('privado3','privado3');
	
			especificos[93] = new Array('numGrupos3','numGrupos3');
			especificos[94] = new Array('a3','a3');
			especificos[95] = new Array('b3','b3');
			especificos[96] = new Array('c3','c3');
			especificos[97] = new Array('d3','d3');
			especificos[98] = new Array('e3','e3');
			especificos[99] = new Array('numTotalIgualdad3','numTotalIgualdad3');
			especificos[100] = new Array('numCursos43','numCursos43');
			especificos[101] = new Array('a43','a43');
			especificos[102] = new Array('b43','b43');
			especificos[103] = new Array('c43','c43');
			especificos[104] = new Array('d43','d43');
			especificos[105] = new Array('e43','e43');
			especificos[106] = new Array('numCursos53','numCursos53');
			especificos[107] = new Array('a53','a53');
			especificos[108] = new Array('b53','b53');
			especificos[109] = new Array('c53','c53');
			especificos[110] = new Array('d53','d53');
			especificos[111] = new Array('e53','e53');
			especificos[112] = new Array('numCursos63','numCursos63');
			especificos[113] = new Array('a63','a63');
			especificos[114] = new Array('b63','b63');
			especificos[115] = new Array('c63','c63');
			especificos[116] = new Array('d63','d63');
			especificos[117] = new Array('e63','e63');
			especificos[118] = new Array('numTotalIntercultura3','numTotalIntercultura3');

			//Cuarto centro escolar

			especificos[119] = new Array('nombreCentro4','nombreCentro4');
			especificos[120] = new Array('direccionCentro4','direccionCentro4');
			especificos[121] = new Array('telefonoCentro4','telefonoCentro4');
			especificos[122] = new Array('faxCentro4','faxCentro4');
			especificos[123] = new Array('emailCentro4','emailCentro4');
			especificos[124] = new Array('contactoCentro4','contactoCentro4');
			especificos[125] = new Array('cargoCentro4','cargoCentro4');

			especificos[126] = new Array('publico4','publico4');
			especificos[127] = new Array('concertado4','concertado4');
			especificos[128] = new Array('privado4','privado4');
			
			especificos[129] = new Array('numGrupos4','numGrupos4');
			especificos[130] = new Array('a4','a4');
			especificos[131] = new Array('b4','b4');
			especificos[132] = new Array('c4','c4');
			especificos[133] = new Array('d4','d4');
			especificos[134] = new Array('e4','e4');
			especificos[135] = new Array('numTotalIgualdad4','numTotalIgualdad4');
			especificos[136] = new Array('numCursos44','numCursos44');
			especificos[137] = new Array('a44','a44');
			especificos[138] = new Array('b44','b44');
			especificos[139] = new Array('c44','c44');
			especificos[140] = new Array('d44','d44');
			especificos[141] = new Array('e44','e44');
			especificos[142] = new Array('numCursos54','numCursos54');
			especificos[143] = new Array('a54','a54');
			especificos[144] = new Array('b54','b54');
			especificos[145] = new Array('c54','c54');
			especificos[146] = new Array('d54','d54');
			especificos[147] = new Array('e54','e54');
			especificos[148] = new Array('numCursos64','numCursos64');
			especificos[149] = new Array('a64','a64');
			especificos[150] = new Array('b64','b64');
			especificos[151] = new Array('c64','c64');
			especificos[152] = new Array('d64','d64');
			especificos[153] = new Array('e64','e64');
			especificos[154] = new Array('numTotalIntercultura4','numTotalIntercultura4');

			//Quinto centro escolar

			especificos[155] = new Array('nombreCentro5','nombreCentro5');
			especificos[156] = new Array('direccionCentro5','direccionCentro5');
			especificos[157] = new Array('telefonoCentro5','telefonoCentro5');
			especificos[158] = new Array('faxCentro5','faxCentro5');
			especificos[159] = new Array('emailCentro5','emailCentro5');
			especificos[160] = new Array('contactoCentro5','contactoCentro5');
			especificos[161] = new Array('cargoCentro5','cargoCentro5');

			especificos[162] = new Array('publico5','publico5');
			especificos[163] = new Array('concertado5','concertado5');
			especificos[164] = new Array('privado5','privado5');
			
			especificos[165] = new Array('numGrupos5','numGrupos5');
			especificos[166] = new Array('a5','a5');
			especificos[167] = new Array('b5','b5');
			especificos[168] = new Array('c5','c5');
			especificos[169] = new Array('d5','d5');
			especificos[170] = new Array('e5','e5');
			especificos[171] = new Array('numTotalIgualdad5','numTotalIgualdad5');
			especificos[172] = new Array('numCursos45','numCursos45');
			especificos[173] = new Array('a45','a45');
			especificos[174] = new Array('b45','b45');
			especificos[175] = new Array('c45','c45');
			especificos[176] = new Array('d45','d45');
			especificos[177] = new Array('e45','e45');
			especificos[178] = new Array('numCursos55','numCursos55');
			especificos[179] = new Array('a55','a55');
			especificos[180] = new Array('b55','b55');
			especificos[181] = new Array('c55','c55');
			especificos[182] = new Array('d55','d55');
			especificos[183] = new Array('e55','e55');
			especificos[184] = new Array('numCursos65','numCursos65');
			especificos[185] = new Array('a65','a65');
			especificos[186] = new Array('b65','b65');
			especificos[187] = new Array('c65','c65');
			especificos[188] = new Array('d65','d65');
			especificos[189] = new Array('e65','e65');
			especificos[190] = new Array('numTotalIntercultura5','numTotalIntercultura5');
			
			//Sexto centro escolar

			especificos[191] = new Array('nombreCentro6','nombreCentro6');
			especificos[192] = new Array('direccionCentro6','direccionCentro6');
			especificos[193] = new Array('telefonoCentro6','telefonoCentro6');
			especificos[194] = new Array('faxCentro6','faxCentro6');
			especificos[195] = new Array('emailCentro6','emailCentro6');
			especificos[196] = new Array('contactoCentro6','contactoCentro6');
			especificos[197] = new Array('cargoCentro6','cargoCentro6');

			especificos[198] = new Array('publico6','publico6');
			especificos[199] = new Array('concertado6','concertado6');
			especificos[200] = new Array('privado6','privado6');
			
			especificos[201] = new Array('numGrupos6','numGrupos6');
			especificos[202] = new Array('a6','a6');
			especificos[203] = new Array('b6','b6');
			especificos[204] = new Array('c6','c6');
			especificos[205] = new Array('d6','d6');
			especificos[206] = new Array('e6','e6');
			especificos[207] = new Array('numTotalIgualdad6','numTotalIgualdad6');
			especificos[208] = new Array('numCursos46','numCursos46');
			especificos[209] = new Array('a46','a46');
			especificos[210] = new Array('b46','b46');
			especificos[211] = new Array('c46','c46');
			especificos[212] = new Array('d46','d46');
			especificos[213] = new Array('e46','e46');
			especificos[214] = new Array('numCursos56','numCursos56');
			especificos[215] = new Array('a56','a56');
			especificos[216] = new Array('b56','b56');
			especificos[217] = new Array('c56','c56');
			especificos[218] = new Array('d56','d56');
			especificos[219] = new Array('e56','e56');
			especificos[220] = new Array('numCursos66','numCursos66');
			especificos[221] = new Array('a66','a66');
			especificos[222] = new Array('b66','b66');
			especificos[223] = new Array('c66','c66');
			especificos[224] = new Array('d66','d66');
			especificos[225] = new Array('e66','e66');
			especificos[226] = new Array('numTotalIntercultura6','numTotalIntercultura6');

			//Séptimo centro escolar

			especificos[227] = new Array('nombreCentro7','nombreCentro7');
			especificos[228] = new Array('direccionCentro7','direccionCentro7');
			especificos[229] = new Array('telefonoCentro7','telefonoCentro7');
			especificos[230] = new Array('faxCentro7','faxCentro7');
			especificos[231] = new Array('emailCentro7','emailCentro7');
			especificos[232] = new Array('contactoCentro7','contactoCentro7');
			especificos[233] = new Array('cargoCentro7','cargoCentro7');

			especificos[234] = new Array('publico7','publico7');
			especificos[235] = new Array('concertado7','concertado7');
			especificos[236] = new Array('privado7','privado7');
			
			especificos[237] = new Array('numGrupos7','numGrupos7');
			especificos[238] = new Array('a7','a7');
			especificos[239] = new Array('b7','b7');
			especificos[240] = new Array('c7','c7');
			especificos[241] = new Array('d7','d7');
			especificos[242] = new Array('e7','e7');
			especificos[243] = new Array('numTotalIgualdad7','numTotalIgualdad7');
			especificos[244] = new Array('numCursos47','numCursos47');
			especificos[245] = new Array('a47','a47');
			especificos[246] = new Array('b47','b47');
			especificos[247] = new Array('c47','c47');
			especificos[248] = new Array('d47','d47');
			especificos[249] = new Array('e47','e47');
			especificos[250] = new Array('numCursos57','numCursos57');
			especificos[251] = new Array('a57','a57');
			especificos[252] = new Array('b57','b57');
			especificos[253] = new Array('c57','c57');
			especificos[254] = new Array('d57','d57');
			especificos[255] = new Array('e57','e57');
			especificos[256] = new Array('numCursos67','numCursos67');
			especificos[257] = new Array('a67','a67');
			especificos[258] = new Array('b67','b67');
			especificos[259] = new Array('c67','c67');
			especificos[260] = new Array('d67','d67');
			especificos[261] = new Array('e67','e67');
			especificos[262] = new Array('numTotalIntercultura7','numTotalIntercultura7');

			//Octavo centro escolar

			especificos[263] = new Array('nombreCentro8','nombreCentro8');
			especificos[264] = new Array('direccionCentro8','direccionCentro8');
			especificos[265] = new Array('telefonoCentro8','telefonoCentro8');
			especificos[266] = new Array('faxCentro8','faxCentro8');
			especificos[267] = new Array('emailCentro8','emailCentro8');
			especificos[268] = new Array('contactoCentro8','contactoCentro8');
			especificos[269] = new Array('cargoCentro8','cargoCentro8');

			especificos[270] = new Array('publico8','publico8');
			especificos[271] = new Array('concertado8','concertado8');
			especificos[272] = new Array('privado8','privado8');
			
			especificos[273] = new Array('numGrupos8','numGrupos8');
			especificos[274] = new Array('a8','a8');
			especificos[275] = new Array('b8','b8');
			especificos[276] = new Array('c8','c8');
			especificos[277] = new Array('d8','d8');
			especificos[278] = new Array('e8','e8');
			especificos[279] = new Array('numTotalIgualdad8','numTotalIgualdad8');
			especificos[280] = new Array('numCursos48','numCursos48');
			especificos[281] = new Array('a48','a48');
			especificos[282] = new Array('b48','b48');
			especificos[283] = new Array('c48','c48');
			especificos[284] = new Array('d48','d48');
			especificos[285] = new Array('e48','e48');
			especificos[286] = new Array('numCursos58','numCursos58');
			especificos[287] = new Array('a58','a58');
			especificos[288] = new Array('b58','b58');
			especificos[289] = new Array('c58','c58');
			especificos[290] = new Array('d58','d58');
			especificos[291] = new Array('e58','e58');
			especificos[292] = new Array('numCursos68','numCursos68');
			especificos[293] = new Array('a68','a68');
			especificos[294] = new Array('b68','b68');
			especificos[295] = new Array('c68','c68');
			especificos[296] = new Array('d68','d68');
			especificos[297] = new Array('e68','e68');
			especificos[298] = new Array('numTotalIntercultura8','numTotalIntercultura8');

			especificos[299] = new Array('declaro1','declaro1');
			especificos[300] = new Array('texto_declaro1','texto_declaro1');
			especificos[301] = new Array('declaro2','declaro2');
			especificos[302] = new Array('texto_declaro2','texto_declaro2');
			especificos[303] = new Array('declaro3','declaro3');
			especificos[304] = new Array('texto_declaro3','texto_declaro3');

			especificos[305] = new Array('convocatoria','convocatoria');
			especificos[306] = new Array('numColegios','numColegios');

			especificos[307] = new Array('nombreSolicitanteH','nombreSolicitanteH');
			especificos[308] = new Array('documentoIdentidadH','documentoIdentidadH');


			// Se añaden los campos del Bullying			
			especificos[309] = new Array('contactoCentro21','contactoCentro21');
			especificos[310] = new Array('contactoCentro31','contactoCentro31');		
			especificos[311] = new Array('contactoCentro41','contactoCentro41');
			especificos[312] = new Array('numGrupos21','numGrupos21');
			especificos[313] = new Array('a21','a21');
			especificos[314] = new Array('b21','b21');
			especificos[315] = new Array('c21','c21');
			especificos[316] = new Array('d21','d21');
			especificos[317] = new Array('e21','e21');
			especificos[318] = new Array('contactoCentro22','contactoCentro22');		
			especificos[319] = new Array('contactoCentro32','contactoCentro32');		
			especificos[320] = new Array('contactoCentro42','contactoCentro42');
			especificos[321] = new Array('numGrupos22','numGrupos22');
			especificos[322] = new Array('a22','a22');
			especificos[323] = new Array('b22','b22');
			especificos[324] = new Array('c22','c22');
			especificos[325] = new Array('d22','d22');
			especificos[326] = new Array('e22','e22');
			especificos[327] = new Array('contactoCentro23','contactoCentro23');		
			especificos[328] = new Array('contactoCentro33','contactoCentro33');		
			especificos[329] = new Array('contactoCentro43','contactoCentro43');
			especificos[330] = new Array('numGrupos23','numGrupos23');
			especificos[331] = new Array('a23','a23');
			especificos[332] = new Array('b23','b23');
			especificos[333] = new Array('c23','c23');
			especificos[334] = new Array('d23','d23');
			especificos[335] = new Array('e23','e23');			
			especificos[336] = new Array('contactoCentro24','contactoCentro24');		
			especificos[337] = new Array('contactoCentro34','contactoCentro34');		
			especificos[338] = new Array('contactoCentro44','contactoCentro44');
			especificos[339] = new Array('numGrupos24','numGrupos24');
			especificos[340] = new Array('a24','a24');
			especificos[341] = new Array('b24','b24');
			especificos[342] = new Array('c24','c24');
			especificos[343] = new Array('d24','d24');
			especificos[344] = new Array('e24','e24');			
			especificos[345] = new Array('contactoCentro25','contactoCentro25');		
			especificos[346] = new Array('contactoCentro35','contactoCentro35');		
			especificos[347] = new Array('contactoCentro45','contactoCentro45');
			especificos[348] = new Array('numGrupos25','numGrupos25');
			especificos[349] = new Array('a25','a25');
			especificos[350] = new Array('b25','b25');
			especificos[351] = new Array('c25','c25');
			especificos[352] = new Array('d25','d25');
			especificos[353] = new Array('e25','e25');
			especificos[354] = new Array('contactoCentro26','contactoCentro26');		
			especificos[355] = new Array('contactoCentro36','contactoCentro36');		
			especificos[356] = new Array('contactoCentro46','contactoCentro46');
			especificos[357] = new Array('numGrupos26','numGrupos26');
			especificos[358] = new Array('a26','a26');
			especificos[359] = new Array('b26','b26');
			especificos[360] = new Array('c26','c26');
			especificos[361] = new Array('d26','d26');
			especificos[362] = new Array('e26','e26');
			especificos[363] = new Array('contactoCentro27','contactoCentro27');		
			especificos[364] = new Array('contactoCentro37','contactoCentro37');		
			especificos[365] = new Array('contactoCentro47','contactoCentro47');
			especificos[366] = new Array('numGrupos27','numGrupos27');
			especificos[367] = new Array('a27','a27');
			especificos[368] = new Array('b27','b27');
			especificos[369] = new Array('c27','c27');
			especificos[370] = new Array('d27','d27');
			especificos[371] = new Array('e27','e27');
			especificos[372] = new Array('contactoCentro28','contactoCentro28');		
			especificos[373] = new Array('contactoCentro38','contactoCentro38');		
			especificos[374] = new Array('contactoCentro48','contactoCentro48');
			especificos[375] = new Array('numGrupos28','numGrupos28');
			especificos[376] = new Array('a28','a28');
			especificos[377] = new Array('b28','b28');
			especificos[378] = new Array('c28','c28');
			especificos[379] = new Array('d28','d28');
			especificos[380] = new Array('e28','e28');
			especificos[381] = new Array('numTotalBullying1','numTotalBullying1');
			especificos[382] = new Array('numTotalBullying2','numTotalBullying2');
			especificos[383] = new Array('numTotalBullying3','numTotalBullying3');
			especificos[384] = new Array('numTotalBullying4','numTotalBullying4');
			especificos[385] = new Array('numTotalBullying5','numTotalBullying5');
			especificos[386] = new Array('numTotalBullying6','numTotalBullying6');
			especificos[387] = new Array('numTotalBullying7','numTotalBullying7');
			especificos[388] = new Array('numTotalBullying8','numTotalBullying8');

			especificos[389] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[390] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');


			<xsl:call-template name="ADD_ROW" />
			<xsl:call-template name="VALIDACIONES" />
			<xsl:call-template name="VALIDACIONES_ESPECIFICAS" />

			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombreSolicitanteH').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('documentoIdentidadH').value = document.getElementById('documentoIdentidad').value;

				document.getElementById('emailSolicitante').value = document.getElementById('d_email_repre').value;

				if(document.getElementById('convocatoria').value=='-------'){
					alert('Debe seleccionar una convocatoria');
					document.getElementById('convocatoria').focus();
					return false;
				}

				var validaNif = valida_nif_cif_nie(document.getElementById('nif_repre'));
				if(validaNif != 1)
					if(validaNif != 2)
						if(validaNif != 3){
							alert('El NIF del representante es incorrecto');
							document.getElementById('nif_repre').focus();			
							return false;
						}

				if(!validar_eCorreos()){
					return false;
				}

				if(!validar_numeros()){
					return false;
				}	

				if(!validar_talleres_solicitados()){
					return false;
				}
				
				if(document.getElementById('numColegios').value==''){
					document.getElementById('numColegios').value = '1';
				}			

				return true;
			}
			
			
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>	

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cargo"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">cargo</xsl:attribute>
					<xsl:attribute name="id">cargo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cargo"/></xsl:attribute>
				</input>
			</div>	
	
			<input type="hidden">
				<xsl:attribute name="name">emailSolicitante</xsl:attribute>
				<xsl:attribute name="id">emailSolicitante</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.convocatoria"/></h1>
   		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
			   		<b><xsl:value-of select="$lang.seleccionar"/></b>				
				</label>
				<br/>
				<xsl:variable name="convocatoria" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosConsulta('Convocatoria', 'select numexp, asunto from spac_expedientes where codprocedimiento IN (#CAMP-ERR-VG#) and fcierre is null and estadoadm=#PR#','001')"/>
				<xsl:variable name="convocatoria" select="document($convocatoria)"/>
			
				<select class="gr">
					<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">convocatoria</xsl:attribute>
					<xsl:attribute name="id">convocatoria</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/convocatoria"/></xsl:attribute>
					<option>
						<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------</option>
						<xsl:for-each select="$convocatoria/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>				
				</select>
			</div>
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.ayto"/></h1>
   		</div>
   		<div class="cuadro" style="">
<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','001')"/>
	
			<xsl:variable name="b" select="document($fileAyuntam)"/>					
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ayto"/>:*
				</label>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select>
					<xsl:attribute name="style">width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>	
			</div>
		</div>


		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosRepresentante"/>
			<font style="font-size:9px;"><xsl:value-of select="$lang.datosRepresentante2"/></font></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">nif_repre</xsl:attribute>
					<xsl:attribute name="id">nif_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nif_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre_repre</xsl:attribute>
					<xsl:attribute name="id">nombre_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cargo_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">cargo_repre</xsl:attribute>
					<xsl:attribute name="id">cargo_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cargo_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.domicilio_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">domicilio_repre</xsl:attribute>
					<xsl:attribute name="id">domicilio_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/domicilio_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">ciudad_repre</xsl:attribute>
					<xsl:attribute name="id">ciudad_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ciudad_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">c_postal_repre</xsl:attribute>
					<xsl:attribute name="id">c_postal_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/c_postal_repre"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">region_repre</xsl:attribute>
					<xsl:attribute name="id">region_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/region_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">movil_repre</xsl:attribute>
					<xsl:attribute name="id">movil_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/movil_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">d_email_repre</xsl:attribute>
					<xsl:attribute name="id">d_email_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/d_email_repre"/></xsl:attribute>
				</input>
			</div>
   		</div>
   		   		
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.centros"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<table style="font-size:11px; margin:0 auto 0  auto; " cellspacing="0" cellpadding="10" border="1" width="93%">
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="COLEGIO"><xsl:with-param name="row_id">8</xsl:with-param></xsl:call-template>
			</table>
			<div style="margin:5px 0 20px 10px; font-size:11px;">
	              		<a id="addRow_link" style="cursor:pointer; color:#0040FF;" onclick="addRow()"><xsl:value-of select="$lang.añadir_centro"/><font style="font-size:10px; color:#08298A;"><xsl:value-of select="$lang.maximo"/></font></a>
				<input type="hidden">
					<xsl:attribute name="id">numColegios</xsl:attribute>
					<xsl:attribute name="name">numColegios</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numColegios"/></xsl:attribute>
				</input>
       			</div>
		</div>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
		</div>

		<xsl:call-template name="DECLARACIONES"/>

		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
		   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				<xsl:value-of select="$lang.anexarInfo1"/>
				<br/>
				<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
			<br/><br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SERSO_VG_D1</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SERSO_VG_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SERSO_VG_D2</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SERSO_VG_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SERSO_VG_D3</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SERSO_VG_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SERSO_VG_D4</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SERSO_VG_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPG</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoXSIG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SERSO_VG_D5</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SERSO_VG_D5_Tipo</xsl:attribute>
					<xsl:attribute name="id">SERSO_VG_D5_Tipo</xsl:attribute>
					<xsl:attribute name="value">XSIG</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
		<input type="hidden">
			<xsl:attribute name="name">nombreSolicitanteH</xsl:attribute>
			<xsl:attribute name="id">nombreSolicitanteH</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<input type="hidden">
			<xsl:attribute name="name">documentoIdentidadH</xsl:attribute>
			<xsl:attribute name="id">documentoIdentidadH</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
