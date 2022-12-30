package com.gestion.empleados.util.reportes;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.gestion.empleados.entidades.Empleado;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class EmpleadoExporterPDF {

	private List<Empleado> listaEmpleados;

	public EmpleadoExporterPDF(List<Empleado> listaEmpleados) {//loq ue hara primeroe s incializarlo
		super();
		this.listaEmpleados = listaEmpleados;
	}
	
	//y una vez lo incialize voya  llamr a los demas metodos para generar elr eporte

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {// aqui lo que hace es crear la cabecera de la tabla
		PdfPCell celda = new PdfPCell();
		celda.setBackgroundColor(Color.pink);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.BLUE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);// con esto te va el ID

		celda.setPhrase(new Phrase("Nombre", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Apellidos", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Email", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Fecha", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Telefono", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Sexo", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Salario", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTable(PdfPTable tabla) {// traer los datos de a bdd o de la lista de empleados
		for (Empleado empleado : listaEmpleados) {
			tabla.addCell(String.valueOf(empleado.getId()));
			tabla.addCell(empleado.getNombre());
			tabla.addCell(empleado.getApellidos());
			tabla.addCell(empleado.getEmail());
			tabla.addCell(empleado.getFecha().toString());
			tabla.addCell(String.valueOf(empleado.getTelefono()));
			tabla.addCell(empleado.getSexo());
			tabla.addCell(String.valueOf(empleado.getSalario()));

		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLUE);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("Lista de empleado", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(8);// le vamos apsar 8 columnas
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		// para la primara columna del ID 1f xq no est tan grande
		// es como el ancho de cada columna
		tabla.setWidths(new float[] { 1f, 2.3f, 2.3f, 6f, 2.9f, 3.5f, 2f, 2.2f });
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTable(tabla);

		documento.add(tabla);
		documento.close();
		
		//ya tenemos nuestra clase configurada
	}

}
