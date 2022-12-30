package com.gestion.empleados.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.empleados.entidades.Empleado;
import com.gestion.empleados.servicio.EmpleadoService;
import com.gestion.empleados.util.paginacion.PageRender;
import com.gestion.empleados.util.reportes.EmpleadoExporterExcel;
import com.gestion.empleados.util.reportes.EmpleadoExporterPDF;
import com.lowagie.text.DocumentException;

@Controller
public class EmpleadoController {

 @Autowired
 private EmpleadoService empleadoService;
 
 //ahora vamos hacer las operaciones crud
 @GetMapping("/ver/{id}")
 public String verDatallesDelEmpleado(@PathVariable (value= "id") Long id,Map<String, Object> modelo, RedirectAttributes flash) {
	 Empleado empleado = empleadoService.findOne(id);
	 
	 if(empleado == null) {
		 flash.addFlashAttribute("error", "empleado no existe en la base de datos");
		 return "redirect:/listar";//siempre y cuando no exista el empleado
	 }
	 
	 //en caso de que si haya
	 modelo.put("empleado", empleado);
	 modelo.put("titulo", "Detalles del empleado "+ empleado.getNombre());
	 return "ver";
	 
 }
 
 @GetMapping({"/","/listar",""})
 //requestparam enviarle un parametro en la solicitud
 //cada vez que ingresemo a la ruta de arriba le enviamos u parametro oage
 public String listarEmpleados(@RequestParam(name ="page", defaultValue = "0" ) int page, Model model){
	 Pageable pageRequest = PageRequest.of(page, 5);//solo va a mostrar 5 listas(5 listados)
	 Page<Empleado> empleados = empleadoService.findAll(pageRequest);//indicar cuantas paginas nos va a mostrar
	 PageRender<Empleado> pageRender = new PageRender<>("/listar", empleados);//pagina que hemos crado para reendirizar
	 
	 model.addAttribute("titulo","Listado  de empleados") ;
	 model.addAttribute("empleados",empleados );//pasar laas paginas de empleado
	 model.addAttribute("page",pageRender );//que tiene las paginas
	 return "listar";
 }
 
 
 @GetMapping("/form")
 public String mostrarFormularioDeRegistrarEmpleado(Map<String, Object> modelo) {
	 Empleado empleado = new Empleado();
	 modelo.put("empleado", empleado);//le estamos enviando un nuevo objeto,un nuevo empleado que lo vamos a guardar
	 modelo.put("titulo", "Registro de empleados");
	 return "form";
 }
 
//bingiresult obtiene todos los resultado cuando se validad los atributos
 @PostMapping("/form")
	public String guardarEmpleado(@Valid Empleado empleado,BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de cliente");
			return "form";
		}
		
		String mensaje = (empleado.getId() != null) ? "El empleado ha sido editato con exito" : "Empleado registrado con exito";
		
		empleadoService.save(empleado);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}


@GetMapping("/form/{id}")//cuando entre a editar se va a mostra el id
//yo siempre tengoq ue buscar su id, path es la ruta
public String editarEmpleado(@PathVariable (value ="id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
	Empleado empleado=null;
			
			if(id>0) {
				empleado = empleadoService.findOne(id);//loe stoy buscando por el id
				
				if(empleado == null) {//si el empleado que estoy buscando es nulo
					flash.addFlashAttribute("error", "El id del empelado no existe en la base de datos");
					return "redirect:/listar";
				}
			}else {
				flash.addFlashAttribute("error", "El id del empleado no puede ser 0");
				return "redirect:/listar";
			}
			
			
	
	modelo.put("empleado", empleado);
	modelo.put("titulo", "Edición del empleado");
	return "form";
}


@GetMapping("/eliminar/{id}")
public String eliminarEmpleado(@PathVariable (value ="id") Long id, RedirectAttributes flash ) {
	if(id>0) {
		empleadoService.delete(id);
		flash.addFlashAttribute("succes", "Empleado eliminado con exito");
		
	}
	return "redirect:/listar";
}


@GetMapping("/exportarPDF")
public void exportarListadoPDF(HttpServletResponse response) throws DocumentException, IOException {
	
	response.setContentType("application/pdf");//aqui le estoy indicando el tipo de contenido a va a devolver
	
	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//aqui le estamos indicando el formatod e fecha actual	
	String fechaActual = dateFormatter.format(new Date());
	
	
	String cabecera = "Content-Disposition";//(lo de arriba) para que al momnetod e indicarle el conten diposition en el attachemnt aparezca la fehca acutal de hoy
	String valor = "attachment; filename=Empleados_" + fechaActual + ".pdf";
	//attachement --> descarhar archivo
	
	response.setHeader(cabecera, valor);//luego le pasamos la cabecera de valor
	
	List<Empleado> empleado=empleadoService.findAll();//hacemo un listado alos empelado que estan en la tabla
	
	EmpleadoExporterPDF exporter = new EmpleadoExporterPDF(empleado);//y le pasamos la clase exporter que creamos
	exporter.exportar(response);
	
}


@GetMapping("/exportarExcel")
public void exportarListadoExcel(HttpServletResponse response) throws DocumentException, IOException {
	
	response.setContentType("application/octec-srteam");//aqui le estoy indicando el tipo de contenido a va a devolver y así me va a responder en tipo excel
	
	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//aqui le estamos indicando el formatod e fecha actual	
	String fechaActual = dateFormatter.format(new Date());
	
	
	String cabecera = "Content-Disposition";//(lo de arriba) para que al momnetod e indicarle el conten diposition en el attachemnt aparezca la fehca acutal de hoy
	String valor = "attachment; filename=Empleados_" + fechaActual + ".xlsx";
	//attachement --> descarhar archivo
	
	response.setHeader(cabecera, valor);//luego le pasamos la cabecera de valor
	
	List<Empleado> empleado=empleadoService.findAll();//hacemo un listado alos empelado que estan en la tabla
	
	EmpleadoExporterExcel exporter = new EmpleadoExporterExcel(empleado);//y le pasamos la clase exporter que creamos
	exporter.exportar(response);
	
}

	
}
