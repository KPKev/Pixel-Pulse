package com.pixelpulse.health.data.repository

import com.pixelpulse.health.data.local.dao.UserDao
import com.pixelpulse.health.data.local.entity.UserEntity
import com.pixelpulse.health.domain.model.User
import com.pixelpulse.health.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun createUser(user: User): Result<User> {
        return try {
            val userEntity = user.toEntity()
            userDao.insertUser(userEntity)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val userEntity = userDao.getUserById(userId)
            val user = userEntity?.toDomainModel()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val userEntity = user.toEntity()
            userDao.updateUser(userEntity)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            userDao.deleteUserById(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        return userDao.getCurrentUser().map { it?.toDomainModel() }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            userDao.getUserCount() > 0
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            // Clear user data logic here
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Extension functions for mapping between domain and data models
private fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        dateOfBirth = dateOfBirth,
        gender = gender.name,
        height = height,
        weight = weight,
        activityLevel = activityLevel.name,
        fitnessGoals = fitnessGoals.map { it.name },
        healthConditions = healthConditions,
        medications = medications,
        emergencyContactName = emergencyContact?.name,
        emergencyContactPhone = emergencyContact?.phoneNumber,
        emergencyContactRelationship = emergencyContact?.relationship,
        units = preferences.units.name,
        notificationPreferences = "", // JSON serialization needed
        privacyPreferences = "", // JSON serialization needed
        theme = preferences.theme.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        email = email,
        name = name,
        dateOfBirth = dateOfBirth,
        gender = Gender.valueOf(gender),
        height = height,
        weight = weight,
        activityLevel = ActivityLevel.valueOf(activityLevel),
        fitnessGoals = fitnessGoals.map { FitnessGoal.valueOf(it) },
        healthConditions = healthConditions,
        medications = medications,
        emergencyContact = if (emergencyContactName != null && emergencyContactPhone != null) {
            EmergencyContact(
                name = emergencyContactName,
                phoneNumber = emergencyContactPhone,
                relationship = emergencyContactRelationship ?: "Unknown"
            )
        } else null,
        preferences = UserPreferences(
            units = Units.valueOf(units),
            theme = ThemePreference.valueOf(theme)
        ),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 