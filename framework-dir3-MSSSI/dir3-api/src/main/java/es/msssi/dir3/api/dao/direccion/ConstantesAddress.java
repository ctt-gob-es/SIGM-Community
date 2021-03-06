package es.msssi.dir3.api.dao.direccion;

import java.util.HashMap;
import java.util.Map;

public class ConstantesAddress {
	
	public static final Map<String, String> PAISES = new HashMap<String, String>();
	public static final Map<String, String> COMUNIADES_AUTONOMAS = new HashMap<String, String>();
	public static final Map<String, String> PROVINCIAS = new HashMap<String, String>();
	public static final Map<String, String> TIPOS_DE_VIA= new HashMap<String, String>();
	
	static{
		TIPOS_DE_VIA.put("18", "Avenida");
		TIPOS_DE_VIA.put("1", "Alameda");
		TIPOS_DE_VIA.put("19", "Avinguda");
		TIPOS_DE_VIA.put("20", "Barrio");
		TIPOS_DE_VIA.put("41", "Bulevar");
		TIPOS_DE_VIA.put("2", "Calle");
		TIPOS_DE_VIA.put("21", "Calleja");
		TIPOS_DE_VIA.put("22", "Cam�");
		TIPOS_DE_VIA.put("3", "Camino");
		TIPOS_DE_VIA.put("23", "Campo");
		TIPOS_DE_VIA.put("4", "Carrer");
		TIPOS_DE_VIA.put("24", "Carrera");
		TIPOS_DE_VIA.put("5", "Carretera");
		TIPOS_DE_VIA.put("25", "Cuesta");
		TIPOS_DE_VIA.put("26", "Edificio");
		TIPOS_DE_VIA.put("27", "Enparantza");
		TIPOS_DE_VIA.put("28", "Estrada");
		TIPOS_DE_VIA.put("6", "Glorieta");
		TIPOS_DE_VIA.put("29", "Jardines");
		TIPOS_DE_VIA.put("30", "Jardins");
		TIPOS_DE_VIA.put("7", "Kalea");
		TIPOS_DE_VIA.put("99", "Otros");
		TIPOS_DE_VIA.put("31", "Parque");
		TIPOS_DE_VIA.put("8", "Pasaje");
		TIPOS_DE_VIA.put("9", "Paseo");
		TIPOS_DE_VIA.put("40", "Passatge");
		TIPOS_DE_VIA.put("32", "Passeig");
		TIPOS_DE_VIA.put("10", "Pla�a");
		TIPOS_DE_VIA.put("35", "Placeta");
		TIPOS_DE_VIA.put("11", "Plaza");
		TIPOS_DE_VIA.put("34", "Plazuela");
		TIPOS_DE_VIA.put("36", "Poblado");
		TIPOS_DE_VIA.put("42", "Pol�gono");
		TIPOS_DE_VIA.put("33", "Praza");
		TIPOS_DE_VIA.put("12", "Rambla");
		TIPOS_DE_VIA.put("13", "Ronda");
		TIPOS_DE_VIA.put("14", "R�a");
		TIPOS_DE_VIA.put("15", "Sector");
		TIPOS_DE_VIA.put("16", "Traves�a");
		TIPOS_DE_VIA.put("38", "Travessera");
		TIPOS_DE_VIA.put("17", "Urbanizaci�n");
		TIPOS_DE_VIA.put("37", "Via");


	}
	
	static{
		PROVINCIAS.put("01", "�lava");
		PROVINCIAS.put("02", "Albacete");
		PROVINCIAS.put("03", "Alicante");
		PROVINCIAS.put("04", "Almer�a");
		PROVINCIAS.put("05", "�vila");
		PROVINCIAS.put("06", "Badajoz");
		PROVINCIAS.put("07", "Baleares");
		PROVINCIAS.put("08", "Barcelona");
		PROVINCIAS.put("09", "Burgos");
		PROVINCIAS.put("10", "C�ceres");
		PROVINCIAS.put("11", "C�diz");
		PROVINCIAS.put("12", "Castell�n");
		PROVINCIAS.put("13", "Ciudad Real");
		PROVINCIAS.put("14", "C�rdoba");
		PROVINCIAS.put("15", "A Coru�a");
		PROVINCIAS.put("16", "Cuenca");
		PROVINCIAS.put("17", "Girona");
		PROVINCIAS.put("18", "Granada");
		PROVINCIAS.put("19", "Guadalajara");
		PROVINCIAS.put("20", "Guip�zcoa");
		PROVINCIAS.put("21", "Huelva");
		PROVINCIAS.put("22", "Huesca");
		PROVINCIAS.put("23", "Ja�n");
		PROVINCIAS.put("24", "Le�n");
		PROVINCIAS.put("25", "Lleida");
		PROVINCIAS.put("26", "La Rioja");
		PROVINCIAS.put("27", "Lugo");
		PROVINCIAS.put("28", "Madrid");
		PROVINCIAS.put("29", "M�laga");
		PROVINCIAS.put("30", "Murcia");
		PROVINCIAS.put("31", "Navarra");
		PROVINCIAS.put("32", "Ourense");
		PROVINCIAS.put("33", "Asturias");
		PROVINCIAS.put("34", "Palencia");
		PROVINCIAS.put("35", "Las Palmas");
		PROVINCIAS.put("36", "Pontevedra");
		PROVINCIAS.put("37", "Salamanca");
		PROVINCIAS.put("38", "Santa Cruz de Tenerife");
		PROVINCIAS.put("39", "Cantabria");
		PROVINCIAS.put("40", "Segovia");
		PROVINCIAS.put("41", "Sevilla");
		PROVINCIAS.put("42", "Soria");
		PROVINCIAS.put("43", "Tarragona");
		PROVINCIAS.put("44", "Teruel");
		PROVINCIAS.put("45", "Toledo");
		PROVINCIAS.put("46", "Valencia");
		PROVINCIAS.put("47", "Valladolid");
		PROVINCIAS.put("48", "Vizcaya");
		PROVINCIAS.put("49", "Zamora");
		PROVINCIAS.put("50", "Zaragoza");
		PROVINCIAS.put("51", "Ceuta");
		PROVINCIAS.put("52", "Melilla");
		
	}
	
	static{
		COMUNIADES_AUTONOMAS.put("01", "Andaluc�a");
		COMUNIADES_AUTONOMAS.put("02", "Arag�n");
		COMUNIADES_AUTONOMAS.put("03", "Principado de Asturias");
		COMUNIADES_AUTONOMAS.put("04", "Illes Balears");
		COMUNIADES_AUTONOMAS.put("05", "Canarias");
		COMUNIADES_AUTONOMAS.put("06", "Cantabria");
		COMUNIADES_AUTONOMAS.put("07", "Castilla y Le�n");
		COMUNIADES_AUTONOMAS.put("08", "Castilla-La Mancha");
		COMUNIADES_AUTONOMAS.put("09", "Catalu�a");
		COMUNIADES_AUTONOMAS.put("10", "Comunitat Valenciana");
		COMUNIADES_AUTONOMAS.put("11", "Extremadura");
		COMUNIADES_AUTONOMAS.put("12", "Galicia");
		COMUNIADES_AUTONOMAS.put("13", "Comunidad de Madrid");
		COMUNIADES_AUTONOMAS.put("14", "Regi�n de Murcia");
		COMUNIADES_AUTONOMAS.put("15", "Comunidad Foral de Navarra");
		COMUNIADES_AUTONOMAS.put("16", "Pa�s Vasco");
		COMUNIADES_AUTONOMAS.put("17", "La Rioja");
		COMUNIADES_AUTONOMAS.put("18", "Ciudad Aut�noma de Ceuta");
		COMUNIADES_AUTONOMAS.put("19", "Ciudad Aut�noma de Melilla");
	}
	
    static{
    	PAISES.put("724", "Espa�a");
    	PAISES.put("248", "AALAND ISLANDS");
    	PAISES.put("4", "AFGHANISTAN");
    	PAISES.put("8", "ALBANIA");
    	PAISES.put("12", "ALGERIA");
    	PAISES.put("16", "AMERICAN SAMOA");
    	PAISES.put("20", "ANDORRA");
    	PAISES.put("24", "ANGOLA");
    	PAISES.put("660", "ANGUILLA");
    	PAISES.put("10", "ANTARCTICA");
    	PAISES.put("28", "ANTIGUA AND BARBUDA");
    	PAISES.put("32", "ARGENTINA");
    	PAISES.put("51", "ARMENIA");
    	PAISES.put("533", "ARUBA");
    	PAISES.put("36", "AUSTRALIA");
    	PAISES.put("40", "AUSTRIA");
    	PAISES.put("31", "AZERBAIJAN");
    	PAISES.put("44", "BAHAMAS");
    	PAISES.put("48", "BAHRAIN");
    	PAISES.put("50", "BANGLADESH");
    	PAISES.put("52", "BARBADOS");
    	PAISES.put("112", "BELARUS");
    	PAISES.put("56", "BELGIUM");
    	PAISES.put("84", "BELIZE");
    	PAISES.put("204", "BENIN");
    	PAISES.put("60", "BERMUDA");
    	PAISES.put("64", "BHUTAN");
    	PAISES.put("68", "BOLIVIA");
    	PAISES.put("70", "BOSNIA AND HERZEGOWINA");
    	PAISES.put("72", "BOTSWANA");
    	PAISES.put("74", "BOUVET ISLAND");
    	PAISES.put("76", "BRAZIL");
    	PAISES.put("86", "BRITISH INDIAN OCEAN TERRITORY");
    	PAISES.put("96", "BRUNEI DARUSSALAM");
    	PAISES.put("100", "BULGARIA");
    	PAISES.put("854", "BURKINA FASO");
    	PAISES.put("108", "BURUNDI");
    	PAISES.put("116", "CAMBODIA");
    	PAISES.put("120", "CAMEROON");
    	PAISES.put("124", "CANADA");
    	PAISES.put("132", "CAPE VERDE");
    	PAISES.put("136", "CAYMAN ISLANDS");
    	PAISES.put("140", "CENTRAL AFRICAN REPUBLIC");
    	PAISES.put("148", "CHAD");
    	PAISES.put("152", "CHILE");
    	PAISES.put("156", "CHINA");
    	PAISES.put("162", "CHRISTMAS ISLAND");
    	PAISES.put("166", "COCOS (KEELING) ISLANDS");
    	PAISES.put("170", "COLOMBIA");
    	PAISES.put("174", "COMOROS");
    	PAISES.put("180", "CONGO, Democratic Republic of (was Zaire)");
    	PAISES.put("178", "CONGO, Republic of");
    	PAISES.put("184", "COOK ISLANDS");
    	PAISES.put("188", "COSTA RICA");
    	PAISES.put("384", "COTE D'IVOIRE");
    	PAISES.put("191", "CROATIA (local name: Hrvatska)");
    	PAISES.put("192", "CUBA");
    	PAISES.put("196", "CYPRUS");
    	PAISES.put("203", "CZECH REPUBLIC");
    	PAISES.put("208", "DENMARK");
    	PAISES.put("262", "DJIBOUTI");
    	PAISES.put("212", "DOMINICA");
    	PAISES.put("214", "DOMINICAN REPUBLIC");
    	PAISES.put("218", "ECUADOR");
    	PAISES.put("818", "EGYPT");
    	PAISES.put("222", "EL SALVADOR");
    	PAISES.put("226", "EQUATORIAL GUINEA");
    	PAISES.put("232", "ERITREA");
    	PAISES.put("233", "ESTONIA");
    	PAISES.put("231", "ETHIOPIA");
    	PAISES.put("238", "FALKLAND ISLANDS (MALVINAS)");
    	PAISES.put("234", "FAROE ISLANDS");
    	PAISES.put("242", "FIJI");
    	PAISES.put("246", "FINLAND");
    	PAISES.put("250", "FRANCE");
    	PAISES.put("254", "FRENCH GUIANA");
    	PAISES.put("258", "FRENCH POLYNESIA");
    	PAISES.put("260", "FRENCH SOUTHERN TERRITORIES");
    	PAISES.put("266", "GABON");
    	PAISES.put("270", "GAMBIA");
    	PAISES.put("268", "GEORGIA");
    	PAISES.put("276", "GERMANY");
    	PAISES.put("288", "GHANA");
    	PAISES.put("292", "GIBRALTAR");
    	PAISES.put("300", "GREECE");
    	PAISES.put("304", "GREENLAND");
    	PAISES.put("308", "GRENADA");
    	PAISES.put("312", "GUADELOUPE");
    	PAISES.put("316", "GUAM");
    	PAISES.put("320", "GUATEMALA");
    	PAISES.put("324", "GUINEA");
    	PAISES.put("624", "GUINEA-BISSAU");
    	PAISES.put("328", "GUYANA");
    	PAISES.put("332", "HAITI");
    	PAISES.put("334", "HEARD AND MC DONALD ISLANDS");
    	PAISES.put("340", "HONDURAS");
    	PAISES.put("344", "HONG KONG");
    	PAISES.put("348", "HUNGARY");
    	PAISES.put("352", "ICELAND");
    	PAISES.put("356", "INDIA");
    	PAISES.put("360", "INDONESIA");
    	PAISES.put("364", "IRAN (ISLAMIC REPUBLIC OF)");
    	PAISES.put("368", "IRAQ");
    	PAISES.put("372", "IRELAND");
    	PAISES.put("376", "ISRAEL");
    	PAISES.put("380", "ITALY");
    	PAISES.put("388", "JAMAICA");
    	PAISES.put("392", "JAPAN");
    	PAISES.put("400", "JORDAN");
    	PAISES.put("398", "KAZAKHSTAN");
    	PAISES.put("404", "KENYA");
    	PAISES.put("296", "KIRIBATI");
    	PAISES.put("408", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF");
    	PAISES.put("410", "KOREA, REPUBLIC OF");
    	PAISES.put("414", "KUWAIT");
    	PAISES.put("417", "KYRGYZSTAN");
    	PAISES.put("418", "LAO PEOPLE'S DEMOCRATIC REPUBLIC");
    	PAISES.put("428", "LATVIA");
    	PAISES.put("422", "LEBANON");
    	PAISES.put("426", "LESOTHO");
    	PAISES.put("430", "LIBERIA");
    	PAISES.put("434", "LIBYAN ARAB JAMAHIRIYA");
    	PAISES.put("438", "LIECHTENSTEIN");
    	PAISES.put("440", "LITHUANIA");
    	PAISES.put("442", "LUXEMBOURG");
    	PAISES.put("446", "MACAU");
    	PAISES.put("807", "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF");
    	PAISES.put("450", "MADAGASCAR");
    	PAISES.put("454", "MALAWI");
    	PAISES.put("458", "MALAYSIA");
    	PAISES.put("462", "MALDIVES");
    	PAISES.put("466", "MALI");
    	PAISES.put("470", "MALTA");
    	PAISES.put("584", "MARSHALL ISLANDS");
    	PAISES.put("474", "MARTINIQUE");
    	PAISES.put("478", "MAURITANIA");
    	PAISES.put("480", "MAURITIUS");
    	PAISES.put("175", "MAYOTTE");
    	PAISES.put("484", "MEXICO");
    	PAISES.put("583", "MICRONESIA, FEDERATED STATES OF");
    	PAISES.put("498", "MOLDOVA, REPUBLIC OF");
    	PAISES.put("492", "MONACO");
    	PAISES.put("496", "MONGOLIA");
    	PAISES.put("500", "MONTSERRAT");
    	PAISES.put("504", "MOROCCO");
    	PAISES.put("508", "MOZAMBIQUE");
    	PAISES.put("104", "MYANMAR");
    	PAISES.put("516", "NAMIBIA");
    	PAISES.put("520", "NAURU");
    	PAISES.put("524", "NEPAL");
    	PAISES.put("528", "NETHERLANDS");
    	PAISES.put("530", "NETHERLANDS ANTILLES");
    	PAISES.put("540", "NEW CALEDONIA");
    	PAISES.put("554", "NEW ZEALAND");
    	PAISES.put("558", "NICARAGUA");
    	PAISES.put("562", "NIGER");
    	PAISES.put("566", "NIGERIA");
    	PAISES.put("570", "NIUE");
    	PAISES.put("574", "NORFOLK ISLAND");
    	PAISES.put("580", "NORTHERN MARIANA ISLANDS");
    	PAISES.put("578", "NORWAY");
    	PAISES.put("512", "OMAN");
    	PAISES.put("586", "PAKISTAN");
    	PAISES.put("585", "PALAU");
    	PAISES.put("275", "PALESTINIAN TERRITORY, Occupied");
    	PAISES.put("591", "PANAMA");
    	PAISES.put("598", "PAPUA NEW GUINEA");
    	PAISES.put("600", "PARAGUAY");
    	PAISES.put("604", "PERU");
    	PAISES.put("608", "PHILIPPINES");
    	PAISES.put("612", "PITCAIRN");
    	PAISES.put("616", "POLAND");
    	PAISES.put("620", "PORTUGAL");
    	PAISES.put("630", "PUERTO RICO");
    	PAISES.put("634", "QATAR");
    	PAISES.put("638", "REUNION");
    	PAISES.put("642", "ROMANIA");
    	PAISES.put("643", "RUSSIAN FEDERATION");
    	PAISES.put("646", "RWANDA");
    	PAISES.put("654", "SAINT HELENA");
    	PAISES.put("659", "SAINT KITTS AND NEVIS");
    	PAISES.put("662", "SAINT LUCIA");
    	PAISES.put("666", "SAINT PIERRE AND MIQUELON");
    	PAISES.put("670", "SAINT VINCENT AND THE GRENADINES");
    	PAISES.put("882", "SAMOA");
    	PAISES.put("674", "SAN MARINO");
    	PAISES.put("678", "SAO TOME AND PRINCIPE");
    	PAISES.put("682", "SAUDI ARABIA");
    	PAISES.put("686", "SENEGAL");
    	PAISES.put("891", "SERBIA AND MONTENEGRO");
    	PAISES.put("690", "SEYCHELLES");
    	PAISES.put("694", "SIERRA LEONE");
    	PAISES.put("702", "SINGAPORE");
    	PAISES.put("703", "SLOVAKIA");
    	PAISES.put("705", "SLOVENIA");
    	PAISES.put("90", "SOLOMON ISLANDS");
    	PAISES.put("706", "SOMALIA");
    	PAISES.put("710", "SOUTH AFRICA");
    	PAISES.put("239", "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS");
    	PAISES.put("144", "SRI LANKA");
    	PAISES.put("736", "SUDAN");
    	PAISES.put("740", "SURINAME");
    	PAISES.put("744", "SVALBARD AND JAN MAYEN ISLANDS");
    	PAISES.put("748", "SWAZILAND");
    	PAISES.put("752", "SWEDEN");
    	PAISES.put("756", "SWITZERLAND");
    	PAISES.put("760", "SYRIAN ARAB REPUBLIC");
    	PAISES.put("158", "TAIWAN");
    	PAISES.put("762", "TAJIKISTAN");
    	PAISES.put("834", "TANZANIA, UNITED REPUBLIC OF");
    	PAISES.put("764", "THAILAND");
    	PAISES.put("626", "TIMOR-LESTE");
    	PAISES.put("768", "TOGO");
    	PAISES.put("772", "TOKELAU");
    	PAISES.put("776", "TONGA");
    	PAISES.put("780", "TRINIDAD AND TOBAGO");
    	PAISES.put("788", "TUNISIA");
    	PAISES.put("792", "TURKEY");
    	PAISES.put("795", "TURKMENISTAN");
    	PAISES.put("796", "TURKS AND CAICOS ISLANDS");
    	PAISES.put("798", "TUVALU");
    	PAISES.put("800", "UGANDA");
    	PAISES.put("804", "UKRAINE");
    	PAISES.put("784", "UNITED ARAB EMIRATES");
    	PAISES.put("826", "UNITED KINGDOM");
    	PAISES.put("840", "UNITED STATES");
    	PAISES.put("581", "UNITED STATES MINOR OUTLYING ISLANDS");
    	PAISES.put("858", "URUGUAY");
    	PAISES.put("860", "UZBEKISTAN");
    	PAISES.put("548", "VANUATU");
    	PAISES.put("336", "VATICAN CITY STATE (HOLY SEE)");
    	PAISES.put("862", "VENEZUELA");
    	PAISES.put("704", "VIET NAM");
    	PAISES.put("92", "VIRGIN ISLANDS (BRITISH)");
    	PAISES.put("850", "VIRGIN ISLANDS (U.S.)");
    	PAISES.put("876", "WALLIS AND FUTUNA ISLANDS");
    	PAISES.put("732", "WESTERN SAHARA");
    	PAISES.put("887", "YEMEN");
    	PAISES.put("894", "ZAMBIA");
    	PAISES.put("716", "ZIMBABWE");
    } 
	
	private ConstantesAddress() {
	}
}
