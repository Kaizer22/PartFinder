package ru.desh.partfinder.core.domain.model

data class AdFile(
    val uid: String,
    val fileName: String,
    val fileType: AdFileType,
    val downloadLink: String,
    val creationTimestamp: Long,
    val sizeBytes: Long,
) {
    // Describes all supported file extensions
    enum class AdFileType {
        XLSX, PPTX, DOC, DOCX
    }
}