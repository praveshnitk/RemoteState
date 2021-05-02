package com.ratnasagar.remotestate

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ratnasagar.remotestate.adapter.TrackingListAdapter
import com.ratnasagar.remotestate.databinding.ActivityMainBinding
import com.ratnasagar.remotestate.factory.MainViewModelFactory
import com.ratnasagar.remotestate.helper.DataBaseHelper
import com.ratnasagar.remotestate.helper.PreferenceHelper
import com.ratnasagar.remotestate.model.TrackingData
import com.ratnasagar.remotestate.receiver.TrackingAlarmReceiver
import com.ratnasagar.remotestate.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity() {
    public lateinit var mainViewModel: MainViewModel
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var pHelper: PreferenceHelper
    private lateinit var binding: ActivityMainBinding
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 10000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_main)
        supportActionBar!!.title = "Remote State Tracking"
        binding.recyclerView.setHasFixedSize(true)
        pHelper = PreferenceHelper(this)
        dbHelper = DataBaseHelper(this)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)
        val mLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.setLayoutManager(mLayoutManager)
        binding.tvTrack.setText("Tracking " + pHelper.getString("StartStop", ""))
        binding.tvStartTime.setText(pHelper.getString("StartTime", ""))
        binding.tvStopTime.setText(pHelper.getString("StopTime", ""))

        binding.btnStartLocation.setOnClickListener(View.OnClickListener {
            if (pHelper.getString("StartStop", "") == "Stop" || pHelper.getString("StartStop", "") == "") {
                try {
                    AlertDialog.Builder(this)
                            .setTitle("Start Tracking")
                            .setMessage("Do you want to start tracking?")
                            .setCancelable(false)
                            .setIcon(R.mipmap.ic_launcher)
                            .setNegativeButton(
                                    "No"
                            ) { dialog, id -> dialog.cancel() }
                            .setPositiveButton("Yes"
                            ) { dialog, which ->
                                val intent = Intent(this, TrackingAlarmReceiver::class.java)
                                val pendingIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_ONE_SHOT)
                                val mgr = MyApplication.getInstance().baseContext.getSystemService(ALARM_SERVICE) as AlarmManager
                                mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = pendingIntent
                                pHelper.setString("StartStop", "Start")
                                pHelper.setString("StartTime", getDateAndTime())
                                pHelper.setString("StopTime", "")
                                binding.tvTrack.setText("Tracking " + pHelper.getString("StartStop", ""))
                                binding.tvStartTime.setText(pHelper.getString("StartTime", ""))
                                binding.tvStopTime.setText("")
                            }.show()
                } catch (tx: Throwable) {
                }
            } else {
                Toast.makeText(this, "Tracking already start you can stop tracking.", Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnStopLocation.setOnClickListener(View.OnClickListener {
            if (pHelper.getString("StartStop", "") == "Start") {
                try {
                    AlertDialog.Builder(this)
                            .setTitle("Start Tracking")
                            .setMessage("Do you want to stop tracking?")
                            .setCancelable(false)
                            .setIcon(R.mipmap.ic_launcher)
                            .setNegativeButton(
                                    "No"
                            ) { dialog, id -> dialog.cancel() }
                            .setPositiveButton("Yes"
                            ) { dialog, which ->
                                val intent = Intent(this, TrackingAlarmReceiver::class.java)
                                val pendingIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_ONE_SHOT)
                                val mgr = MyApplication.getInstance().baseContext.getSystemService(ALARM_SERVICE) as AlarmManager
                                mgr.cancel(pendingIntent)
                                pHelper.setString("StartStop", "Stop")
                                pHelper.setString("StopTime", getDateAndTime())
                                binding.tvTrack.setText("Tracking " + pHelper.getString("StartStop", ""))
                                binding.tvStopTime.setText(pHelper.getString("StopTime", ""))
                            }.show()
                } catch (tx: Throwable) {
                }
            } else {
                Toast.makeText(this, "Tracking already stop you can start tracking.", Toast.LENGTH_SHORT).show()
            }
        })
        mainViewModel.topicsMutableLiveData.observe(this, androidx.lifecycle.Observer<ArrayList<TrackingData>> { result ->
            val trackingData = result
            val trackingListAdapter = TrackingListAdapter(trackingData, this)
            binding.recyclerView.setAdapter(trackingListAdapter)
        })
        binding.btnReload.setOnClickListener {
            callSetUpAdapter()
        }
        callSetUpAdapter()
    }

    private fun callSetUpAdapter() {
        mainViewModel.getTrackData(this, dbHelper)
        /*val trackingData = dbHelper.trackAll
        val trackingListAdapter = TrackingListAdapter(trackingData, this)
        binding.recyclerView.setAdapter(trackingListAdapter)*/
    }
    private fun getDateAndTime(): String? {
        var Datetime = ""
        try {
            val AmOrPm: String
            val c = Calendar.getInstance()
            val dates = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val am_pm = c[Calendar.AM_PM]
            AmOrPm = if (am_pm == 0) {
                "AM"
            } else {
                "PM"
            }
            Datetime = dates.format(c.time) + AmOrPm
        } catch (ex: Exception) {
        }
        return Datetime
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            callSetUpAdapter()
            /*Toast.makeText(this, "This method is run every 10 seconds",
                    Toast.LENGTH_SHORT).show()*/
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
}


