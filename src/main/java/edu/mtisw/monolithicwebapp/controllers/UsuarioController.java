package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.UsuarioEntity;
import edu.mtisw.monolithicwebapp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping
public class UsuarioController {
    @Autowired
	UsuarioService usuarioService;

    @GetMapping("/listar")
	public String listar(Model model) {
    	ArrayList<UsuarioEntity>usuarios=usuarioService.obtenerUsuarios();
    	model.addAttribute("usuarios",usuarios);
		return "index";
	}

}