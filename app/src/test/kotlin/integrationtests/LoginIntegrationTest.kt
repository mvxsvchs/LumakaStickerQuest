package integrationtests

import com.example.lumaka.data.repository.ApiResult
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.ui.feature.login.LoginViewModel
import com.example.lumaka.domain.model.Login
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginIntegrationTest {

    private val mockUserRepo: UserRepository = mock()
    private val mockPointsRepo: PointsRepository = mock()
    private val mockSessionRepo: SessionRepository = mock()

    @Test
    fun loginFlow_withValidCredentials_shouldSetSuccess() = runTest {
        // Simuliere erfolgreichen Login
        whenever(mockUserRepo.loginUser(Login("test@mail.com", "password")))
            .thenReturn(ApiResult.Success(data = com.example.lumaka.domain.model.User("test@mail.com", 0)))

        val vm = LoginViewModel(mockUserRepo, mockPointsRepo, mockSessionRepo)

        vm.onLogin("test@mail.com", "password")

        assertTrue(vm.uiState.value.isSuccess)
    }

    @Test
    fun loginFlow_withInvalidCredentials_shouldSetError() = runTest {
        // Simuliere fehlgeschlagenen Login
        whenever(mockUserRepo.loginUser(Login("wrong@mail.com", "password")))
            .thenReturn(ApiResult.Error(statusCode = 401, isNetworkError = false))

        val vm = LoginViewModel(mockUserRepo, mockPointsRepo, mockSessionRepo)

        vm.onLogin("wrong@mail.com", "password")

        assertTrue(vm.uiState.value.errorMessageId != null)
    }
}
