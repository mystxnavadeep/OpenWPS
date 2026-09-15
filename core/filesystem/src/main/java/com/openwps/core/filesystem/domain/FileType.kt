package com.openwps.core.filesystem.domain

sealed interface FileType {
    data object Document : FileType // .doc, .docx, .odt, .txt, .rtf
    data object Spreadsheet : FileType // .xls, .xlsx, .csv, .ods
    data object Presentation : FileType // .ppt, .pptx, .odp
    data object Pdf : FileType // .pdf
    data object Image : FileType // .png, .jpg, .jpeg, .webp, .gif, .svg
    data object Archive : FileType // .zip, .rar, .tar, .gz, .7z
    data object Folder : FileType
    data object Unknown : FileType
}
