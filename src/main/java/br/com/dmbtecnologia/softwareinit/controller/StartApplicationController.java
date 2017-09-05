package br.com.dmbtecnologia.softwareinit.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.dmbtecnologia.softwareinit.model.Software;

@Controller
public class StartApplicationController {

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("software", new Software());
		return "index";
	}

	@PostMapping("/")
	public String save(@ModelAttribute Software software, Model model) throws IOException {
		if (software.getSoftwareName().isEmpty()) {
			return "index";
		}

		final String[] params = software.getSoftwareName().split(" ");
		final List<String> commands = Arrays.asList(params);

		try {
			final Process process = new ProcessBuilder(commands).start();
			final InputStream is = process.getInputStream();
			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);
			String output = "";

			while (br.readLine() != null) {
				output += br.readLine() + "\n";
			}

			model.addAttribute("output", output);

		} catch (final IOException e) {
			model.addAttribute("error", e.getMessage());
		}

		return "index";
	}

}
