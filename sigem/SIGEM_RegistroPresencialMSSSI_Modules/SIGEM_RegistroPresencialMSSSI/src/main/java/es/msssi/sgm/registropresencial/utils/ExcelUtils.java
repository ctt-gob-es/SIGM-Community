/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import es.msssi.sgm.registropresencial.beans.ibatis.Departament;
import es.msssi.sgm.registropresencial.beans.ibatis.Permission;
import es.msssi.sgm.registropresencial.beans.ibatis.Profile;
import es.msssi.sgm.registropresencial.beans.ibatis.User;

public class ExcelUtils {
    private static final Logger LOG = Logger.getLogger(ExcelUtils.class.getName());
	public static List<User> getListUserFromByteArray(byte[] excelDocument) throws IOException {

		List<User> listUser = null;
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(new ByteArrayInputStream(excelDocument));

			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					listUser = new ArrayList<User>();
					continue;
				}
				
				if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null
						|| row.getCell(4) == null || row.getCell(5) == null || row.getCell(6) == null
						|| row.getCell(7) == null) {
					LOG.debug("Usuario vacío.");
					continue;
				}

				String userUser = row.getCell(0).getStringCellValue();				
				String userName = row.getCell(1).getStringCellValue();
				String userSurname1 = row.getCell(2).getStringCellValue();
				String userSurname2 = row.getCell(3).getStringCellValue();
				String userEmail = row.getCell(4).getStringCellValue();
				int departamentId = (int) row.getCell(5).getNumericCellValue();
				String password = row.getCell(6).getStringCellValue();
				int userGenericPermissions = (int) row.getCell(7).getNumericCellValue();

				if (userUser.isEmpty()|| userName.isEmpty()|| userSurname1.isEmpty()|| userSurname2.isEmpty()|| userEmail.isEmpty()|| userUser.isEmpty()|| 
						departamentId == 0 || password.isEmpty()|| userGenericPermissions == 0) {
					LOG.debug("Usuario vacío.");
					continue;
				}
				
				
				User user = new User();
				user.setUserUser(userUser);
				user.setUserName(userName);
				user.setUserSurname1(userSurname1);
				user.setUserSurname2(userSurname2);
				user.setUserEmail(userEmail);
				Departament userDepartament = new Departament();
				userDepartament.setDepartamentId(departamentId);
				user.setUserDepartament(userDepartament);
				user.setUserPassword(password);
				user.setUserGenericPermissions(userGenericPermissions);
				
				AdminUtils admUtils = new AdminUtils();
				/////////////////// insertPermissions
				List<Permission> permissions = null;
				permissions = admUtils.setDefaultPerms();
				user.setPermissions(permissions);

				/////////////////// insertProfiles
				List<Profile> profiles = null;
				profiles = admUtils.setDefaultProfile(user);
				user.setProfiles(profiles);

				LOG.debug("Datos de usuario: "+user);
				
				listUser.add(user);

			}

		} catch (IOException e) {
			LOG.error("Error leyendo archivo excel.",e);
			e.printStackTrace();
			throw e;
		}

		return listUser;
	}

}
