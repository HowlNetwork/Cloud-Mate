package com.example.cloudmate.screens.setting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.cloudmate.R
import com.example.cloudmate.ui.theme.White
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.widgets.NavBar
import android.net.Uri
import android.provider.Settings


@Composable
fun SettingScreen(
    navController: NavController,
) {

    val context = LocalContext.current
    val activity = context as Activity
    var isSwitchChecked by remember { mutableStateOf(false) }

    // Xử lý kết quả yêu cầu quyền
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                isSwitchChecked = true // Nếu cấp quyền thì chuyển trạng thái của Switch
            } else {
                isSwitchChecked = false // Nếu không cấp quyền thì tắt Switch
                // Kiểm tra xem người dùng đã từ chối quyền nhiều lần chưa và chưa chọn "Không hỏi lại"
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ).not()) {
                    // Người dùng đã từ chối quyền nhiều lần, yêu cầu họ vào cài đặt
                    Toast.makeText(context, "Please go to Settings and enable the permission manually", Toast.LENGTH_LONG).show()
                    // Tạo Intent mở cài đặt ứng dụng
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material.Text(
                        text = "Settings",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        color = White
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                ) {
                    Text(
                        text = "Notifications",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Switch(
                        checked = isSwitchChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                // Kiểm tra quyền và yêu cầu quyền khi bật switch
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // Nếu quyền chưa được cấp, yêu cầu quyền
                                        permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        // Quyền đã được cấp, bật switch
                                        isSwitchChecked = true
                                    }
                                } else {
                                    // Dành cho phiên bản Android thấp hơn
                                    isSwitchChecked = true
                                }
                            } else {
                                // Người dùng tắt Switch
                                isSwitchChecked = false
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray
                        )
                    )
                }
            }
        }, bottomBar = {
            NavBar(navController = navController)
        }, containerColor = Color.Transparent)
    }
}
