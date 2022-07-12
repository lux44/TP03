package com.lux.zena.tp03.activity

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.lux.zena.tp03.R
import com.lux.zena.tp03.databinding.ActivityMainBinding
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var bluetoothGatt:BluetoothGatt? = null
    private val bluetoothManager by lazy { this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    private val bluetoothAdapter:BluetoothAdapter = bluetoothManager.adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fun PackageManager.missingSystemFeature (name : String) :Boolean = !hasSystemFeature(name)

        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show()
            finish()
        }



        val profileListener = object : BluetoothProfile.ServiceListener {
            override fun onServiceConnected(p0: Int, p1: BluetoothProfile?) {
                if (p0==BluetoothProfile.GATT){
                    bluetoothGatt = p1 as BluetoothGatt
                }
            }

            override fun onServiceDisconnected(p0: Int) {
                bluetoothGatt = null
            }

        }

        bluetoothAdapter.getProfileProxy(this, profileListener,BluetoothProfile.GATT)

        if (bluetoothAdapter==null){
            // Device doesn't support Bluetooth
        }else{
            if (!bluetoothAdapter.isEnabled){
                //블루투스 활성화 요청
                var enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                var resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()
                ) {
                    if(it.resultCode==Activity.RESULT_OK){
                        enableIntent= it.data!!
                    }
                }
                resultLauncher.launch(enableIntent)

            }
        }





        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        pairedDevices.forEach{device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
        }

        val filter:IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)


        registerReceiver(receiver, filter)

        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300)
        }
        startActivity(discoverableIntent)



        val bluetoothGattCallback = object : BluetoothGattCallback() {

        }
        binding.btnSignup.setOnClickListener {


        }

        

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){
                BluetoothDevice.ACTION_FOUND ->{
                    val device:BluetoothDevice? = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }else{
                        val deviceName = device?.name
                        val deviceHardewareAddress = device?.address
                    }


                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    private inner class AcceptThread : Thread(){




    }


}