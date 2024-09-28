import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayush.diasconnect_seller.ui.ImagePickerItem
import com.ayush.diasconnect_seller.ui.feature.upload_product.UploadProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadProductScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: UploadProductViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.addImageFromUri(context, uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                items(uiState.selectedImages) { image ->
                    ImagePickerItem(
                        image = image,
                        onRemove = { viewModel.removeImage(it) }
                    )
                }
                item {
                    if (uiState.selectedImages.size < 7) {
                        ImagePickerItem(
                            onAdd = { imagePickerLauncher.launch("image/*") }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProductInputField(
                        value = uiState.productParams.name,
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(name = it)) },
                        label = "Product Name",
                        isError = uiState.productParams.name.isBlank() && uiState.errorMessage != null
                    )
                    ProductInputField(
                        value = uiState.productParams.price.toString(),
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(price = it.toFloatOrNull() ?: 0f)) },
                        label = "Price",
                        keyboardType = KeyboardType.Decimal,
                        isError = uiState.productParams.price <= 0 && uiState.errorMessage != null
                    )
                    ProductInputField(
                        value = uiState.productParams.description,
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(description = it)) },
                        label = "Description",
                        singleLine = false,
                        minLines = 3,
                        isError = uiState.productParams.description.isBlank() && uiState.errorMessage != null
                    )
                    ProductInputField(
                        value = uiState.productParams.stock.toString(),
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(stock = it.toIntOrNull() ?: 0)) },
                        label = "Stock",
                        keyboardType = KeyboardType.Number,
                        isError = uiState.productParams.stock <= 0 && uiState.errorMessage != null
                    )
                    ProductInputField(
                        value = uiState.productParams.categoryId.toString(),
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(categoryId = it.toLongOrNull() ?: 0L)) },
                        label = "Category ID",
                        keyboardType = KeyboardType.Number,
                        isError = uiState.productParams.categoryId <= 0 && uiState.errorMessage != null
                    )
                    ProductInputField(
                        value = uiState.productParams.sellerId.toString(),
                        onValueChange = { viewModel.updateProductParams(uiState.productParams.copy(sellerId = it.toLongOrNull() ?: 0L)) },
                        label = "Seller ID",
                        keyboardType = KeyboardType.Number,
                        isError = uiState.productParams.sellerId <= 0 && uiState.errorMessage != null
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.submitProduct()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Uploading..." else "Upload Product")
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) {
                snackbarHostState.showSnackbar("Product uploaded successfully!")
                 navController.popBackStack()
            }
        }
    }
}

@Composable
fun ProductInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = minLines,
        isError = isError
    )
}