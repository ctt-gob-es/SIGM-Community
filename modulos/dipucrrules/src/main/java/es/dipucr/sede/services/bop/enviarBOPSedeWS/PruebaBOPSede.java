package es.dipucr.sede.services.bop.enviarBOPSedeWS;


public class PruebaBOPSede {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		EnviarBOPSedeWSProxy wsSede = new EnviarBOPSedeWSProxy();
				
		HeaderBOP bop = new HeaderBOP(1, "01/01/2013", 1, 28);
		int idBOP = wsSede.publicarHeaderBOPSede(bop);
		System.out.println(idBOP);
		
		AnuncioBOP anuncio1 = new AnuncioBOP(1, idBOP, "Anuncio 1", 1, 1, "DIPUTACIÓN", "Diputacion", "Licitacionés");
		AnuncioBOP anuncio2 = new AnuncioBOP(2, idBOP, "Anuncio 2", 2, 15, "Aytos", "Ciudad Real", "Subvenciones");
		AnuncioBOP[] anuncios = {anuncio1, anuncio2};
		
		System.out.println(wsSede.publicarAnunciosBOPSede(anuncios));
		
		
	}

}
