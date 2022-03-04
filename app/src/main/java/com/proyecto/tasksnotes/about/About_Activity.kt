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
import java.lang.String


class About_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val versionElement : Element = Element()
        versionElement.setTitle("Version 1.0");
        versionElement.gravity = Gravity.CENTER

        val center  :Element = Element()
        center.setTitle("ILERNA ONLINE")

        val copyRight : Element = Element()
        @SuppressLint("DefaultLocale")
        val string = String.format("Copyright %d by Antonio Piñero", Calendar.getInstance()[Calendar.YEAR])
        copyRight.title = string
        copyRight.gravity = Gravity.CENTER


        val name = Element()
        name.setTitle("Antonio Piñero Sánchez")
        name.gravity = Gravity.CENTER



        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("Aplicación creada como proyecto final del ciclo" +
                    " de grado superior de Desarrollo de Aplicaciones Multiplataforma.")
            .setImage(R.drawable.icono_app_completo_150px)
            .addItem(name)
            .addItem(versionElement)
            .addItem(createSchool())
            .addEmail("anpisan@gmail.com")
            .addItem(addLinkedIn())
            .addTwitter("antoni0ps")
            .addInstagram("antoni0ps/")
            .addItem(copyRight)
            .create()
        setContentView(aboutPage)

    }

    private fun createSchool(): Element? {
        val school = Element()
        school.setTitle("ILERNA Online")
        school.iconDrawable = R.drawable.ilerna_icon

        val url = "https://www.ilerna.es/"
        school.onClickListener = View.OnClickListener {

            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return school
    }

    private fun addLinkedIn() : Element?{

        val linkediIn = Element()
        linkediIn.setTitle("LinkedIn")
        linkediIn.iconDrawable = R.drawable.linkedin_icon

        val url = "https://www.linkedin.com/in/antoniopinerosanchez/"
        linkediIn.setOnClickListener{
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return linkediIn
    }
}


