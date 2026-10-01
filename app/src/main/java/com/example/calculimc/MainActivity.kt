package com.example.calculimc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.round

class MainActivity : ComponentActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener {
            calculerImc()
        }

        buttonEffacer.setOnClickListener {
            effacerFormulaire()
        }
    }

    private fun calculerImc() {
        val texteP = editTextPoids.text.toString().trim()
        val texteT = editTextTaille.text.toString().trim()

        if (texteP.isEmpty() || texteT.isEmpty()) {
            afficherErreur(getString(R.string.erreur_champs_vides))
            return
        }

        val poids = texteP.replace(",", ".").toDoubleOrNull()
        val taille = texteT.replace(",", ".").toDoubleOrNull()

        if (poids == null || taille == null) {
            afficherErreur(getString(R.string.erreur_valeur_invalide))
            return
        }

        if (poids <= 0 || taille <= 0) {
            afficherErreur(getString(R.string.erreur_valeur_negative))
            return
        }

        val imcBrut = poids / (taille * taille)
        val imc = round(imcBrut * 100) / 100

        val (categorie, couleur) = when {
            imc < 18.5 -> getString(R.string.categorie_insuffisance) to R.color.imc_orange
            imc < 25.0 -> getString(R.string.categorie_normale) to R.color.imc_vert
            imc < 30.0 -> getString(R.string.categorie_surpoids) to R.color.imc_orange
            imc < 35.0 -> getString(R.string.categorie_obesite_moderee) to R.color.imc_rouge
            imc < 40.0 -> getString(R.string.categorie_obesite_severe) to R.color.imc_rouge
            else -> getString(R.string.categorie_obesite_morbide) to R.color.imc_rouge_fonce
        }

        textViewImc.text = String.format("%.2f", imc)
        textViewImc.setTextColor(ContextCompat.getColor(this, couleur))
        textViewCategorie.text = categorie
        textViewCategorie.setTextColor(ContextCompat.getColor(this, couleur))
    }

    private fun afficherErreur(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        textViewImc.text = ""
        textViewCategorie.text = ""
    }

    private fun effacerFormulaire() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }
}