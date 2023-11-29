package hu.bme.aut.android.deadliner.screens.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.deadliner.data.calendar.CalendarUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItem(date: CalendarUiModel.Date,
                onDateClickListener: (CalendarUiModel.Date) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { // making the element clickable, by adding 'clickable' modifier
                onDateClickListener(date)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}