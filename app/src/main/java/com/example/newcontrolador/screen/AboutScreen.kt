package com.example.newcontrolador.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.ui.theme.NewControladorTheme
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.TopBar2
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun MainAboutScreen(navController: NavController) {
	val scroll = rememberScrollState()
	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, LocalContext.current)

	Scaffold(
		topBar = { TopBar2("Acerca de", navController) },
		containerColor = MaterialTheme.colorScheme.onBackground
	) { padd ->
		Column(
			modifier = Modifier
				.padding(padd)
				.fillMaxSize()
				.padding(15.dp)
				.verticalScroll(scroll)
		) {
			Row(
				Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Image(
					painter = painterResource(R.drawable.lautarotomasardiles),
					contentDescription = "Foto de Lautaro Tomás Ardiles",
					modifier = Modifier.size(200.dp)
				)
			}
			Spacer(Modifier.height(10.dp))

			Card(
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.background
				),
				border = BorderStroke(
					2.dp,
					MaterialTheme.colorScheme.primary
				),
			) {
				Box(Modifier.padding(10.dp)) {
					MarkdownText(
						markdown = """
						## ¿ Quien soy ?
						Soy **Lautaro Tomás Ardiles**, alumno de la **secundaria técnica N° 4 
						de Garín** en la especialidad de **informática**.
					""".trimIndent(),
						style = TextStyle(color = MaterialTheme.colorScheme.tertiary),
					)
				}
			}
			Spacer(Modifier.height(10.dp))

			Card(
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.background
				),
				border = BorderStroke(
					2.dp,
					MaterialTheme.colorScheme.secondary
				),
			) {
				Box(Modifier.padding(10.dp)) {
					MarkdownText(
						markdown = """
						## Agradecimientos
						
						Quiero agradecer a mis **profesores** de la especialidad de **Informatica**
						por su apoyo a lo largo del tiempo, especialmente a **Villalba Paulo**,
						**Susana Herbas**, **Melanie Serrano**, **Agustin Aguirre** y **Eduardo 
						baldiviezo**
						""".trimIndent(),
						style = TextStyle(color = MaterialTheme.colorScheme.secondary),
					)
				}
			}
			Spacer(Modifier.height(10.dp))

			MarkdownText(
				markdown = """
					## ¿ Qué es esta aplicación ?
					
					Esta **aplicación** es un controlador de movimiento para robots. Tiene una
					**interfaz** con botones **configurables** en alto, ancho y espaciado, además,
					**permite** cambiar las señales que se envían al robot, al presionar los
					botones de movimiento o al cambiar de modo, **desde** la configuración completa.  
		  			
					Incluye agregados **extra** como, **diagramas** y **código** para robots,
					y botones para cambiar de **modo** si el robot lo admite.  
	  
					## ¿ Por qué hice esta aplicación ?
	  
					La **razón** por la que hice esta **aplicación** fue para que alumnos o 
					cualquier persona con la necesidad de manejar **robots**, normalmente robots
					de sumo o de tipo similar, puedan hacerlo con una aplicación **polivalente**,
					que permite manejar robots con varios **métodos de conexión** y con su
					configuración permite cambiar el **aspecto del control**, además de que se
					pueden **cambiar las señales** que se envían desde el control permite que no se
					tenga que hacer el robot alrededor de la aplicación, sino que se puede
					**modificar** la aplicación para ajustarse al **robot**.  
				""".trimIndent(),
				style = TextStyle(color = MaterialTheme.colorScheme.primary),
			)
			Spacer(Modifier.height(10.dp))

			Card(
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.background
				),
				border = BorderStroke(
					2.dp,
					MaterialTheme.colorScheme.primary
				),
			) {
				Box(Modifier.padding(10.dp)) {
					MarkdownText(
						markdown = """
							### **V 2.3.1 (versión actual de la aplicación)**

							## **Historial de versiones:** 
                    		
							### **V 2.3.1:**
                    		
							Esta versión se centró principalmente en correcciones y ajustes menores.
							Se solucionaron errores detectados durante el uso de la aplicación,
							se realizaron mejoras de limpieza en el código y se ajustaron distintos
							detalles relacionados con la estabilidad general, la configuración y la
							visualización de mensajes. El objetivo de esta actualización fue mejorar
							la experiencia de uso sin introducir cambios importantes en el 
							funcionamiento principal.
                    		
							### **V 2.3.0:**
                    		
							Se realizaron mejoras importantes en el sistema de configuración. 
							La administración de opciones fue reorganizada para facilitar su uso y
							mantenimiento. También se añadieron cambios relacionados con la
							configuración persistente, mejoras en los administradores de conexión y 
							optimizaciones en la comunicación con los robots mediante el envío 
							de señales.
                    		
							### **V 2.2.0:**
                    		
							Se agregaron nuevas estructuras de datos para mejorar la administración 
							de configuraciones y estados internos. También se incorporaron 
							enumeraciones y clases específicas para organizar mejor la información 
							utilizada por la aplicación. Esta actualización permitió que el proyecto
							fuera más ordenado, escalable y sencillo de mantener.
                    		
							### **V 2.1.0:**
                    		
							Se incorporaron nuevas mejoras en la organización del código y se 
							actualizó parte de la interfaz principal. Además, se realizaron ajustes 
							internos destinados a mejorar la experiencia de uso y la preparación de 
							nuevas funciones relacionadas con la configuración del controlador.
                    		
							### **V 2.0.0:**
                    		
							Se llevó a cabo una refactorización importante de la aplicación. Gran 
							parte de la estructura interna fue reorganizada para permitir una mayor 
							flexibilidad en el desarrollo futuro. Esta actualización marcó una nueva 
							etapa del proyecto, preparando la base para configuraciones más 
							avanzadas y una mejor administración de los distintos componentes.
                    		
							### **V 1.2.0:**
                    		
							La aplicación comenzó a recibir una reorganización más importante del 
							código. Se mejoró la distribución de componentes internos y se 
							realizaron cambios orientados a simplificar el mantenimiento del 
							proyecto y la incorporación de nuevas funciones.
                    		
							### **V 1.1.0:**
                    		
							Se realizaron mejoras en la organización general del proyecto y en la 
							estructura interna de la aplicación. También se hicieron ajustes 
							relacionados con el control principal para facilitar futuras 
							ampliaciones y mejorar la estabilidad general.
                    		
							### **V 1.0.0:**
                    		
							Primera versión funcional de la aplicación. Se implementó el sistema 
							base de control para robots, permitiendo enviar comandos de movimiento 
							desde una interfaz simple. Esta versión sirvió como punto de partida 
							para el desarrollo del proyecto y para las primeras pruebas de conexión 
							y control.
						""".trimIndent(),
						style = TextStyle(color = MaterialTheme.colorScheme.tertiary),
					)
				}
			}
		}
	}
}

@Preview
@Composable
private fun Ads() {
	NewControladorTheme {
		MainAboutScreen(navController = NavController(LocalContext.current))
	}
}
