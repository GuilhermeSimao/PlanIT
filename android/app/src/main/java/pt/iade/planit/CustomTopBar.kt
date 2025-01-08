package pt.iade.planit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    showBackButton: Boolean = false,
    showLogoutButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        ) },
        navigationIcon = {
            if (showBackButton) {
                run {
                    IconButton(onClick = { onBackClick?.invoke() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            } else null
        },
        actions = {
            if (showLogoutButton) {
                IconButton(onClick = { onLogoutClick?.invoke() }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
