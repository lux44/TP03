package com.lux.zena.tp03.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.lux.zena.tp03.R
import com.lux.zena.tp03.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val bluetoothManager by lazy { this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    lateinit var bluetoothAdapter: BluetoothAdapter





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)



        binding.btnSignup.setOnClickListener {

            // ActivityResultLauncher



            bluetoothAdapter=bluetoothManager.adapter
            if(bluetoothAdapter==null){
                // 블루투스 지원 x
            }else {
                // 블루투스 지원 o
                // 블루투스 활성화
                if(!bluetoothAdapter.isEnabled){
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    val registerForResult = registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ){ result->
                        if (result.resultCode== Activity.RESULT_OK){
                            val intent = result.data
                        }
                    }
                    registerForResult.launch(enableBtIntent)
                }
            }


        }

        

    }


}