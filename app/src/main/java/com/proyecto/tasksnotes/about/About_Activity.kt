package com.proyecto.tasksnotes.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyecto.tasksnotes.databinding.ActivityAboutBinding
import android.view.View
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

import android.view.Gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.proyecto.tasksnotes.R


class About_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val versionElement = Element()
        versionElement.title = "Version 1.0"
        versionElement.gravity = Gravity.CENTER

        val center = Element()
        center.title = "ILERNA ONLINE"

        val copyRight = Element()
        @SuppressLint("DefaultLocale")
        val string = String.format("Copyright %d by Antonio Piñero", Calendar.getInstance()[Calendar.YEAR])
        copyRight.title = string
        copyRight.gravity = Gravity.CENTER


        val name = Element()
        name.title = "Antonio Piñero Sánchez"
        name.gravity = Gravity.CENTER



        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("Aplicación creada como proyecto final del ciclo" +
                    " de grado superior de Desarrollo de Aplicaciones Multiplataforma.\n\n" +
                    "NoteAps es un organizador personal donde puedes almacenar tus notas, tareas " +
                    "y eventos de manera unficada y eficiente.")
            .setImage(R.drawable.icono_app_completo_150px)
            .addItem(name)
            .addItem(versionElement)
            .addItem(createSchool())
            .addEmail("anpisan@gmail.com")
            .addItem(addLinkedIn())
            .addItem(addGithub())
            .addTwitter("antoni0ps")
            .addInstagram("antoni0ps/")
            .addItem(copyRight)
            .create()
        setContentView(aboutPage)

    }

    private fun addGithub(): Element {
        val github = Element()
        github.title = "GitHub"
        github.iconDrawable = R.drawable.github_icon
        github.iconTint = R.color.black_github

        val url = "https://github.com/antoni0ps"
        github.setOnClickListener {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        return github
    }

    private fun createSchool(): Element {
        val school = Element()
        school.title = "ILERNA Online"
        school.iconDrawable = R.drawable.ilerna_icon
        school.iconTint = R.color.black

        val url = "https://www.ilerna.es/"
        school.onClickListener = View.OnClickListener {

            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return school
    }

    private fun addLinkedIn() : Element{

        val linkediIn = Element()
        linkediIn.title = "LinkedIn"
        linkediIn.iconDrawable = R.drawable.linkedin_icon
        linkediIn.iconTint = R.color.blue_linkedin

        val url = "https://www.linkedin.com/in/antoniopinerosanchez/"
        linkediIn.setOnClickListener{
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return linkediIn
    }
}


