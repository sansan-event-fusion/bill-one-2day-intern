import { FileExtension } from "./pdf-generator"

export class FileExtensionError extends Error {
  get name() {
    return this.constructor.name
  }

  constructor(message?: string) {
    super(message)
  }
}

export class NotFoundException extends FileExtensionError {
  constructor(fileName: string) {
    super()

    this.message = `Could not find fileExtension "${fileName}"`
  }
}

export class NotPermitted extends FileExtensionError {
  constructor(fileName: string) {
    super()

    this.message = `Could not find fileExtension "${fileName}"` + `Permit File Extension: "jpeg" | "jpg" | "png"`
  }
}

export const getFileExtensionFromFileName = (fileName: string): FileExtension => {
  const extension = fileName.split(".").pop()?.toLowerCase()
  if (typeof extension === "undefined") {
    throw new NotFoundException(fileName)
  }
  if (!isPermittedFileExtension(extension)) {
    throw new NotPermitted(fileName)
  }
  return extension
}

function isPermittedFileExtension(fileExtension: string): fileExtension is FileExtension {
  return ["png", "jpeg", "jpg"].includes(fileExtension)
}
