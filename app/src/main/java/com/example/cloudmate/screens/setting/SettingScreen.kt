package com.example.cloudmate.screens.setting
import android.Manifest
import android.app.Activity
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.cloudmate.ui.theme.White
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.widgets.NavBar
import androidx.compose.runtime.collectAsState



@Composable
fun SettingScreen(
    navController: NavController,
    settingViewModel: SettingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity

    // Lấy trạng thái Switch từ ViewModel
    val isSwitchChecked by settingViewModel.isNotificationEnabled.collectAsState()

    // Xử lý kết quả yêu cầu quyền
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                settingViewModel.enableNotifications()
                Toast.makeText(context, "Cảnh báo thời tiết đã được bật", Toast.LENGTH_SHORT).show()
            } else {
                settingViewModel.disableNotifications()
                Toast.makeText(context, "Bạn cần cấp quyền để bật thông báo", Toast.LENGTH_LONG).show()
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
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
                                // Yêu cầu quyền nếu chưa được cấp
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    settingViewModel.enableNotifications()
                                }
                            } else {
                                settingViewModel.disableNotifications()
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Gray,
                            checkedTrackColor = Color(0xFF4CAF50),
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

