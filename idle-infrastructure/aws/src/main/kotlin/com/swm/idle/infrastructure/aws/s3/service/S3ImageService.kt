package com.swm.idle.infrastructure.aws.s3.service

import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.infrastructure.aws.s3.properties.S3BucketProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration
import java.util.*

@Service
class S3ImageService(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val s3BucketProperties: S3BucketProperties,
) {

    fun getImageUploadUrl(
        userType: UserType,
        imageId: UUID,
        imageFileExtension: ImageFileExtension,
    ): URL {
        val objectRequest = PutObjectRequest.builder()
            .bucket(s3BucketProperties.userProfileImage.bucketName)
            .key(getKey(userType, imageId, imageFileExtension))
            .build()

        val signatureDuration =
            getDuration(s3BucketProperties.userProfileImage.preSignedUrlExpireMinutes)

        val preSignRequest = PutObjectPresignRequest
            .builder()
            .signatureDuration(signatureDuration)
            .putObjectRequest(objectRequest)
            .build()

        return s3Presigner
            .presignPutObject(preSignRequest)
            .url()
            .toString()
            .let { URL(it) }
    }

    fun getKey(
        userType: UserType,
        imageId: UUID,
        imageFileExtension: ImageFileExtension,
    ): String {
        return "${s3BucketProperties.userProfileImage.keyPrefix}${userType.value}/${imageId}.${imageFileExtension.value}"
    }

    fun getDuration(expireMinutes: Long): Duration {
        return Duration.ofMinutes(expireMinutes)
    }

    fun findByIdAndExtension(
        userType: UserType,
        imageId: UUID,
        imageFileExtension: ImageFileExtension,
    ): URL {
        val getObjectRequest = GetUrlRequest
            .builder()
            .bucket(s3BucketProperties.userProfileImage.bucketName)
            .key(getKey(userType, imageId, imageFileExtension))
            .build()

        return s3Client
            .utilities()
            .getUrl(getObjectRequest)
            .toString()
            .let { URL(it) }
    }

}
