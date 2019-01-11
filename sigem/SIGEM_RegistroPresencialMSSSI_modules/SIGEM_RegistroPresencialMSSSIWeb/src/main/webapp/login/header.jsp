
<link rel="stylesheet" type="text/css"
	href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/css/intranet-sigm.css"
	media="screen,projection" />
<link rel="stylesheet" type="text/css"
	href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/css/intranet-sigm-imprimir.css"
	media="print" />
	<link rel="icon" type="image/png" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/img/favicon.gif" />

</head>

<body>
	<div id="contenedor">

		<!-- Cabecera -->
		<div id="cabecera">
			<div id="escudo">
				<a
					title="Ministerio de Sanidad, Consumo y Bienestar Social - Gobierno de Espa&ntilde;a"
					href="#"><img
					alt="Ministerio de Sanidad, Consumo y Bienestar Social - Gobierno de Espa&ntilde;a"
					src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/img/logo_ministerio.jpg" /></a>
			</div>
			<div id="titulo">
				<h1><%=ResourceLDAP.getInstance().getProperty("tituloAplicacionCabeceraRP")%></h1>
			</div>
		</div>
		<!-- Cabecera -->
		<!-- Menu horizontal -->
		<div id="menuhorizontal">
			<div class="izquierdo">
				<h2><%=ResourceLDAP.getInstance().getProperty("tituloAplicacionRP")%></h2>
			</div>
			<div class="derecho"></div>
		</div>
		<!-- Fin menu horizontal -->
<div id="bloquecontenido" style="
    min-height: 10em;">
		<div id="login">
			<div class="usuario">