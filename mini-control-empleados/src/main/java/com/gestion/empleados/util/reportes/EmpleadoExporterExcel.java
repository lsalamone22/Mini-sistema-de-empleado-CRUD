package com.gestion.empleados.util.reportes;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gestion.empleados.entidades.Empleado;

public class EmpleadoExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Empleado> listaEmpelado;

	public EmpleadoExporterExcel(List<Empleado> listaEmpelado) {

		this.listaEmpelado = listaEmpelado;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Empleado");
	}

	private void escribirCabeceraDeTabla() {// recprdemo que un archivo excel tiene fila para row
		Row fila = hoja.createRow(0);

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);// cabecera negra y true que resalte
		fuente.setFontHeight(16);// altura de la letra
		estilo.setFont(fuente);

		Cell celda = fila.createCell(0);//todo esto seria la cabecera de la tabla en excel
		celda.setCellValue("ID");
		celda.setCellStyle(estilo);

		celda = fila.createCell(1);
		celda.setCellValue("Nombre");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("Apellidos");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("Email");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("Fecha");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("Telefono");
		celda.setCellStyle(estilo);

		celda = fila.createCell(6);
		celda.setCellValue("Sexo");
		celda.setCellStyle(estilo);

		celda = fila.createCell(7);
		celda.setCellValue("Salario");
		celda.setCellStyle(estilo);
	}
	
	private void escribirDatosDeLaTabla() {
		int numeroFilas=1;
		
		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente=libro.createFont();
		fuente.setFontHeight(14);//estamos extrayendo como van hacer los datos
		estilo.setFont(fuente);
		
		for(Empleado empleado :listaEmpelado) {
			Row fila=hoja.createRow(numeroFilas ++);//las filas que van creando es depende del numero de fila
			//digamos que en la rpiemra fila voy a guardar un dato pero que pasa si hay más filas, 
			//si hay mas filas voy a sehuir creando las veces que se van ejecutando este bucle for
		
			
			Cell celda = fila.createCell(0);
			celda.setCellValue(empleado.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(1);
			celda.setCellValue(empleado.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(2);
			celda.setCellValue(empleado.getApellidos());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(3);
			celda.setCellValue(empleado.getEmail());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(4);
			celda.setCellValue(empleado.getFecha().toString());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(5);
			celda.setCellValue(empleado.getTelefono());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(6);
			celda.setCellValue(empleado.getSexo());
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(7);
			celda.setCellValue(empleado.getSalario());
			hoja.autoSizeColumn(7);
			celda.setCellStyle(estilo);
		}
		
	}
	
	public void exportar(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTabla();
		escribirDatosDeLaTabla();
		
		ServletOutputStream outPutStream = response.getOutputStream();
		libro.write(outPutStream);
		
		libro.close();
		outPutStream.close();
	}
}
