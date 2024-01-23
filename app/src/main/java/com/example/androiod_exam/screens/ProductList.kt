package com.example.androiod_exam.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.androiod_exam.CustomTopAppBar
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.viewModel.ProductViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable


fun ProductList(productViewModel: ProductViewModel, navController: NavController) {
    val products by productViewModel.products
// Start oppkall for å hente alle produkter når skjermen startes
    LaunchedEffect(productViewModel) {
        productViewModel.getAllProducts()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 60.dp)
        ) {
            items(products) { product ->
                ProductListItem(product = product) {
                    navController.navigate("${Screen.ProductScreen.route}/${product.id}")
                }
            }
        }
    }
}

@Composable
fun ProductListItem(product: ProductEntity, onItemClick: (ProductEntity) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black)
            .clickable { onItemClick(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Vis produktbildet ved hjelp av CoilImage
            CoilImage(
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                imageUrl = product.image
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Product ID: ${product.id}")
            Text(text = product.title)
            Text(text = "Category: ${product.category}")
            Text(text = "Price: ${product.price} $")
        }
    }
}
// Composable for å vise et bilde ved hjelp av Coil-biblioteket
@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
fun CoilImage(
    contentDescription: String,
    modifier: Modifier,
    contentScale: ContentScale,
    imageUrl: String
) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
        }
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}