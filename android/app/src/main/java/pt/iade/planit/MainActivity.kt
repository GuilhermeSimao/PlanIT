package pt.iade.planit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.iade.planit.ui.theme.PlanITTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanITTheme(dynamicColor = false) {
                Navigation()
            }
        }
    }
}


