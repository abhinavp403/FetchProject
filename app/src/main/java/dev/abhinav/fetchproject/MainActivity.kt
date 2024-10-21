package dev.abhinav.fetchproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.abhinav.fetchproject.ui.theme.FetchProjectTheme
import dev.abhinav.fetchproject.viewmodel.FetchViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayList()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayList(
    modifier: Modifier = Modifier,
    viewModel: FetchViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.createList()
    }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val scroll = TopAppBarDefaults.pinnedScrollBehavior()
    val listState by viewModel.list.collectAsState()
    val listIds = viewModel.getListOfListIds(listState)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "List")
                },
                scrollBehavior = scroll
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {

            if (loadError.isNotEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = loadError,
                        fontSize = 16.sp,
                    )
                }
            }

            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                listIds.forEach { listId ->
                    DisplayItems(listId, viewModel, modifier)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

//Display rows of items for each distinct list id
@Composable
fun DisplayItems(
    i: Int,
    viewModel: FetchViewModel,
    modifier: Modifier = Modifier,
) {
    val list = viewModel.groupByListId(i)
    Text(
        text = "List Id: $i",
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier.fillMaxWidth().padding(4.dp)
    )
    list.forEach {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Id: ${it.id}",
                modifier = modifier.weight(1f)
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(
                text = "Name: ${it.name}",
                modifier = modifier.weight(1f)
            )
        }
    }
}