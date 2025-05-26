package com.pixelpulse.health.data.repository

import com.pixelpulse.health.data.local.dao.UserDao
import com.pixelpulse.health.data.local.entity.UserEntity
import com.pixelpulse.health.domain.model.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat

class UserRepositoryImplTest {

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var userRepository: UserRepositoryImpl

    private val testUser = User(
        id = "test-user-id",
        email = "test@example.com",
        name = "Test User",
        dateOfBirth = kotlinx.datetime.LocalDate(1990, 1, 1),
        gender = Gender.MALE,
        height = 180.0,
        weight = 75.0,
        activityLevel = ActivityLevel.MODERATE,
        fitnessGoals = listOf(FitnessGoal.WEIGHT_LOSS),
        healthConditions = emptyList(),
        medications = emptyList(),
        emergencyContact = EmergencyContact(
            name = "Emergency Contact",
            phoneNumber = "+1234567890",
            relationship = "Family"
        ),
        preferences = UserPreferences(
            units = Units.METRIC,
            theme = ThemePreference.SYSTEM
        ),
        createdAt = LocalDateTime(2024, 1, 1, 0, 0),
        updatedAt = LocalDateTime(2024, 1, 1, 0, 0)
    )

    private val testUserEntity = UserEntity(
        id = "test-user-id",
        email = "test@example.com",
        name = "Test User",
        dateOfBirth = kotlinx.datetime.LocalDate(1990, 1, 1),
        gender = "MALE",
        height = 180.0,
        weight = 75.0,
        activityLevel = "MODERATE",
        fitnessGoals = listOf("WEIGHT_LOSS"),
        healthConditions = emptyList(),
        medications = emptyList(),
        emergencyContactName = "Emergency Contact",
        emergencyContactPhone = "+1234567890",
        emergencyContactRelationship = "Family",
        units = "METRIC",
        theme = "SYSTEM",
        createdAt = LocalDateTime(2024, 1, 1, 0, 0),
        updatedAt = LocalDateTime(2024, 1, 1, 0, 0)
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepositoryImpl(userDao)
    }

    @Test
    fun `createUser should insert user and return success`() = runTest {
        // Given
        whenever(userDao.insertUser(testUserEntity)).thenReturn(Unit)

        // When
        val result = userRepository.createUser(testUser)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(testUser)
        verify(userDao).insertUser(testUserEntity)
    }

    @Test
    fun `createUser should return failure when dao throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(userDao.insertUser(testUserEntity)).thenThrow(exception)

        // When
        val result = userRepository.createUser(testUser)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `getUserById should return user when found`() = runTest {
        // Given
        whenever(userDao.getUserById("test-user-id")).thenReturn(testUserEntity)

        // When
        val result = userRepository.getUserById("test-user-id")

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user).isNotNull()
        assertThat(user?.id).isEqualTo("test-user-id")
        assertThat(user?.email).isEqualTo("test@example.com")
    }

    @Test
    fun `getUserById should return null when user not found`() = runTest {
        // Given
        whenever(userDao.getUserById("non-existent-id")).thenReturn(null)

        // When
        val result = userRepository.getUserById("non-existent-id")

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `updateUser should update user and return success`() = runTest {
        // Given
        whenever(userDao.updateUser(testUserEntity)).thenReturn(Unit)

        // When
        val result = userRepository.updateUser(testUser)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(testUser)
        verify(userDao).updateUser(testUserEntity)
    }

    @Test
    fun `deleteUser should delete user and return success`() = runTest {
        // Given
        whenever(userDao.deleteUserById("test-user-id")).thenReturn(Unit)

        // When
        val result = userRepository.deleteUser("test-user-id")

        // Then
        assertThat(result.isSuccess).isTrue()
        verify(userDao).deleteUserById("test-user-id")
    }

    @Test
    fun `getCurrentUser should return flow of current user`() = runTest {
        // Given
        whenever(userDao.getCurrentUser()).thenReturn(flowOf(testUserEntity))

        // When
        val flow = userRepository.getCurrentUser()

        // Then
        flow.collect { user ->
            assertThat(user).isNotNull()
            assertThat(user?.id).isEqualTo("test-user-id")
        }
    }
} 