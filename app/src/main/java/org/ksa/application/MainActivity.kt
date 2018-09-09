package org.ksa.application

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream
import java.util.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.app.Activity


class MainActivity : AppCompatActivity() {
    private val mutableList = mutableListOf<Container>()
    private val rnd = Random()
    private val mainArray = Array(6, { i -> Container("", "") })
    private var total: Int = 0
    private var correctCount: Int = 0

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyStoragePermissions(this)
        load("/org.ksa/source.csv")
        fillArray()
    }

    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun fillArray() {
        mainArray[0] = mutableList[rnd.nextInt(mutableList.size)]
        mainArray[1] = mutableList[rnd.nextInt(mutableList.size)]
        mainArray[2] = mutableList[rnd.nextInt(mutableList.size)]
        mainArray[3] = mutableList[rnd.nextInt(mutableList.size)]
        mainArray[4] = mutableList[rnd.nextInt(mutableList.size)]
        mainArray[5] = mutableList[rnd.nextInt(mutableList.size)]
        draw(mainArray)
    }

    private fun draw(array: Array<Container>) {
        val qNumb = rnd.nextInt(5)
        setText(btn11, array[0].key)
        setText(btn12, array[1].key)
        setText(btn21, array[2].key)
        setText(btn22, array[3].key)
        setText(btn31, array[4].key)
        setText(btn32, array[5].key)
        question.setText(array[qNumb].value.toCharArray(), 0, array[qNumb].value.length)
    }

    private fun load(path: String) {
        val f = File("" + Environment.getExternalStorageDirectory() + path)
        if (f.exists()) {
            val inputStream: InputStream = f.inputStream()
            inputStream.bufferedReader().useLines { lines -> lines.forEach { line -> mutableList.add(parse(line)) } }
        } else
            f.createNewFile()

    }

    private fun parse(line: String): Container {
        val p: List<String> = line.split(",")
        return Container(p[0], p[1])
    }

    private fun check(pos: Int) {
        val dialog: Toast
        if (question.text.toString().equals(mainArray.get(pos).value)) {
            dialog = Toast.makeText(this, "Correct", Toast.LENGTH_SHORT)
            correctCount++
            fillArray()
        } else {
            dialog = Toast.makeText(this, "Fault", Toast.LENGTH_SHORT)
        }
        total++
        dialog.show()
        result.text = "Attempt/correct - $total/$correctCount"
    }


    fun onBtn11Click(view: View) {
        check(0)
    }

    fun onBtn12Click(view: View) {
        check(1)
    }

    fun onBtn21Click(view: View) {
        check(2)
    }

    fun onBtn22Click(view: View) {
        check(3)
    }

    fun onBtn31Click(view: View) {
        check(4)
    }

    fun onBtn32Click(view: View) {
        check(5)
    }

    fun onBtnNextClick(view: View) {
        fillArray()
    }

    fun setText(btn: Button, text: String) {
        btn.setText(text.toCharArray(), 0, text.length)
    }

    class Container constructor(key: String, value: String) {
        val key = key
        val value = value
    }
}

