package com.ayush.diasconnect_seller.ui.feature.orders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.ayush.diasconnect_seller.ui.feature.home.ErrorView
import com.ayush.diasconnect_seller.ui.feature.home.LoadingView
import diasconnect.seller.com.model.myOrder

@OptIn(ExperimentalMaterial3Api::class)
class OrdersScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: OrderViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val isRefreshing by viewModel.isRefreshing.collectAsState()
        val sortOption by viewModel.sortOption.collectAsState()
        val state = rememberPullToRefreshState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Orders") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        SortMenu(
                            currentSort = sortOption,
                            onSortChanged = { viewModel.setSortOption(it) }
                        )
                    }
                )
            }
        ) { paddingValues ->
            PullToRefreshBox(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() },
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = state,
                        isRefreshing = isRefreshing,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopCenter)
                    )
                }
            ) {
                when (val currentState = uiState) {
                    is OrderScreenUIEvents.Loading -> LoadingView()
                    is OrderScreenUIEvents.Success -> OrderList(orders = currentState.data)
                    is OrderScreenUIEvents.Error -> ErrorView(message = currentState.message) {
                        viewModel.getOrders()
                    }
                }
            }
        }
    }

    @Composable
    fun SortMenu(currentSort: SortOption, onSortChanged: (SortOption) -> Unit) {
        var expanded by remember { mutableStateOf(false) }

        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Sort")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortOption.values().forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onSortChanged(option)
                        expanded = false
                    },
                    leadingIcon = {
                        if (currentSort == option) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun OrderList(orders: List<myOrder>) {
        if (orders.isEmpty()) {
            EmptyOrdersView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    OrderCard(order)
                }
            }
        }
    }

    @Composable
    fun OrderCard(order: myOrder) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (order.status.toString()) {
                    "Completed" -> MaterialTheme.colorScheme.primaryContainer
                    "Processing" -> MaterialTheme.colorScheme.secondaryContainer
                    "Cancelled" -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            var expanded by remember { mutableStateOf(false) }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    StatusChip(status = order.status.toString())
                }
                Text(
                    "Total: ${order.currency} ${order.total}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text("Created: ${order.createdAt}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(if (expanded) "Hide Details" else "Show Details")
                }

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text("Order Items:", style = MaterialTheme.typography.titleSmall)
                        order.items.forEach { item ->
                            Text("- ${item.quantity}x Product #${item.productId} ($${item.price})")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StatusChip(status: String) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = when (status) {
                "Completed" -> MaterialTheme.colorScheme.primary
                "Processing" -> MaterialTheme.colorScheme.secondary
                "Cancelled" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    @Composable
    fun EmptyOrdersView() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No orders available",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}