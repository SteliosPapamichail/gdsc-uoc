package com.gdscuoc.lazycolumngdsc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.recyclersample.Datasource
import com.gdscuoc.lazycolumngdsc.ui.theme.LazyColumnGDSCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnGDSCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val datasource = Datasource(context)
                    FlowerList(datasource)
                }
            }
        }
    }
}

@Composable
fun FlowerList(datasource: Datasource) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(datasource.getFlowerList()) { flower ->
            Text(flower, fontSize = 50.sp)
        }
    }
}