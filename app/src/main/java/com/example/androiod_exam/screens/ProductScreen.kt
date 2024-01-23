package com.example.androiod_exam.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.androiod_exam.CustomTopAppBar
import com.example.androiod_exam.DataClass.Product
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.viewModel.ProductViewModel
import com.example.androiod_exam.viewModel.ShoppingCartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun ProductScreen(
    product: ProductEntity?,
    navController: NavController,
    productViewModel: ProductViewModel,
    shoppingCartViewModel: ShoppingCartViewModel
) {
    if (product != null) {
        Scaffold(
            topBar = { CustomTopAppBar(navController = navController) },
            content = {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(800.dp) // Setter høyden til 800dp
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                            CoilImage(
                                imageUrl = product.image ?: "null Image",
                                contentDescription = "Product Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(26.dp))

                            Text(
                                text = "Product ID: ${product.id ?: "null id"}",
                                style = typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.title,
                                style = typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Category: ${product.category ?: "null category"}",

                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Description: ${product.description ?: "null description"}",

                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Price: ${product.price  ?: "null price"} $",
                            )

                            Button(
                                onClick = {
                                    shoppingCartViewModel.addToCart(product)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(text = "Add to Cart")
                            }
                        }
                    }
                }
            }
        )
    }
}

