package hu.bme.aut.android.deadliner.screens.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.android.deadliner.data.calendar.CalendarUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun Header(data: CalendarUiModel,
           onPrevClickListener: (LocalDate) -> Unit,
           onNextClickListener: (LocalDate) -> Unit,

           ) {
    Row {
        Text(
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                )
            },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {  onPrevClickListener(data.startDate.date) }) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Previous"
            )
        }
        IconButton(onClick = { onNextClickListener(data.endDate.date)}) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next"
            )
        }
    }
}