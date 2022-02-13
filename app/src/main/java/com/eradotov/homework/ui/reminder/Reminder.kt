package com.eradotov.homework.ui.reminder


import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.eradotov.homework.Graph
import com.eradotov.homework.data.repository.ReminderRepository
import com.eradotov.homework.data.entity.Reminder
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date
import java.util.*

/*TODO-ADD ICON PICKER OR PHOTO TAKER*/
@Composable
fun Reminder(
    userId: Long,
    onBackPress: () -> Unit,

){
    var context = LocalContext.current

    /*TODO-EXCLUDE TO OWN CLASS/FUN*/
    //CALENDAR
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("")}
    val datePickerDialog = DatePickerDialog(
        context,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int->
            date.value = "$dayOfMonth.$month.$year"
        }, year, month, day
    )

    val rMessage = rememberSaveable { mutableStateOf("") }
    val rLocationX = rememberSaveable { mutableStateOf("") }
    val rLocationY = rememberSaveable { mutableStateOf("") }
    val rTime = rememberSaveable { mutableStateOf("") }
    val rCreationTime = rememberSaveable { mutableStateOf("") }
    val reminderSeen = rememberSaveable { mutableStateOf("") }

    /*TODO-SWITCH TO VIEW*/
    val reminderRepository: ReminderRepository = Graph.reminderRepository
    val coroutineScope = rememberCoroutineScope()

    Surface{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f),
            ){
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
                Text(
                    text = "Add reminder",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h6
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = rMessage.value,
                    enabled = true,
                    maxLines = 4,
                    onValueChange = {data->rMessage.value=data},
                    label = { Text(text = "Reminder message")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = "Location of reminder...",
                    enabled = false,
                    onValueChange = {},
                    label = { Text(text = "Description")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { datePickerDialog.show()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(45.dp)
                ) {
                    Text(text = "Pick date:${date.value}")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        val reminder = Reminder(
                            rUserId = userId,
                            rMessage = rMessage.value,
                            rTime = date.value,
                            rCreationTime = Date().time
                        )
                        coroutineScope.launch {
                            reminderRepository.addReminder(reminder)
                        }
                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(45.dp)
                ) {
                    Text("Add")
                }
            }
        }
    }
}