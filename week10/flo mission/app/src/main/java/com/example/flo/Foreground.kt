package com.example.flo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.flo.ui.song.SongActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Foreground : Service() {

    //notification 보낼 채널 id
    val ChannelId="123"
    //notification의 id
    val NotiId=456
    //채널 생성
    fun createNotificationChannel(){
        //version 0 이상에서만 동작하기 때문에 버전 체크
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val serviceChannel=NotificationChannel(ChannelId,"FOREGROUND",NotificationManager.IMPORTANCE_DEFAULT)
            //낮은 중요도-소리 안 들려줌, 중간-소리를 들려줌, 높음-소리 진동 팝업 띄워줌 등 강도가 달라짐
            val manager=getSystemService(NotificationManager::class.java) //notification manager를 시스템을 통해 생성
            manager.createNotificationChannel(serviceChannel) //시스템에 해당 채널을 쓰겠다고 알리는 것
        }
    }

//    lateinit var notification:NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        //버전 호환성을 위해 notification compat 사용
        val notification=NotificationCompat.Builder(this,ChannelId)
            .setContentTitle("Foreground Service") //notification에 보여주는 제목
            .setSmallIcon(R.mipmap.ic_launcher_round) //표시할 아이콘


        //눌렀을 때 activiity 이동하는 코드
        val moveAppIntent=Intent(this, SongActivity::class.java)
        moveAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent=PendingIntent.getActivity(this,0,moveAppIntent,PendingIntent.FLAG_MUTABLE)
        notification.setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).apply {
            GlobalScope.launch {
                var count:Int=0
                while (count <= 1000) {
                    count = count + 1
                    Thread.sleep(100)
                    notification.setProgress(1000,count,false)
                    notify(NotiId,notification.build())
                }
                notification.setContentText("complete")
                    .setProgress(0,0,false)
                notify(NotiId,notification.build())
            }
        }

        startForeground(NotiId,notification.build())//notification 생성 및 서비스 시작 한번에
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}