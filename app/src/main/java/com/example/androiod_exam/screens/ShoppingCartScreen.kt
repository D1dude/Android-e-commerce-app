package com.example.androiod_exam.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androiod_exam.CustomTopAppBar
import com.example.androiod_exam.DataClass.entity.CartItemEntity
import com.example.androiod_exam.viewModel.ShoppingCartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(coil.annotation.ExperimentalCoilApi::class)
fun ShoppingCartScreen(
    cartItems: List<CartItemEntity>,
    navController: NavController,
    shoppingCartViewModel: ShoppingCartViewModel,
) {
    val cartItems by shoppingCartViewModel.cartItems.observeAsState(cartItems)

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Shopping Cart",
                    style = typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(16.dp)
                ) {
                    LazyColumn {
                        items(cartItems) { cartItem ->
                            ShoppingCartItem(
                                cartItem = cartItem,
                                onDecreaseClick = {
                                    shoppingCartViewModel.decreaseCartItemQuantity(cartItem)
                                    Log.d("Tag", "Melding")
                                },
                                onIncreaseClick = {
                                    shoppingCartViewModel.increaseCartItemQuantity(cartItem)
                                    Log.d("Tag", "Melding")
                                },
                                onRemoveClick = {
                                    shoppingCartViewModel.removeCartItem(cartItem)
                                    Log.d("Tag", "Melding")
                                },
                                shoppingCartViewModel = shoppingCartViewModel // Pass shoppingCartViewModel som parameter
                            )
                        }
                    }
                }
                val totalPrice by shoppingCartViewModel.totalPrice.observeAsState(initial = 0.0)

                Text(
                    text = "Total: ${totalPrice} $",
                    modifier = Modifier.padding(top = 16.dp),
                    style = typography.titleMedium
                )
                Button(
                    onClick = {
                        Log.d("ShoppingCartViewModel", "Number of items in cart: ${cartItems.size}")
                        // Kall på createOrder-funksjonen når "Buy"-knappen trykkes
                        shoppingCartViewModel.createOrder(cartItems)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Buy")
                }
            }
        }
    )
}



@Composable
fun ShoppingCartItem(
    cartItem: CartItemEntity,
    onRemoveClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    shoppingCartViewModel: ShoppingCartViewModel
) {
    val observedCartItem by shoppingCartViewModel.cartItem.observeAsState(cartItem)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .height(120.dp)
            .border(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "${cartItem.title}")

            Spacer(modifier = Modifier.height(1.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onIncreaseClick, // Endret her
                    modifier = Modifier.width(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(1.dp))

                Text(text = "${observedCartItem.quantity}")

                Spacer(modifier = Modifier.width(1.dp))

                IconButton(
                    onClick = onDecreaseClick, // Endret her
                    modifier = Modifier.width(20.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.BottomEnd)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }

            Text(
                text = "${cartItem.price} $",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}