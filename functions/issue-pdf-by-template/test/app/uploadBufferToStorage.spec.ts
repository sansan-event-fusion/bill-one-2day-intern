import { Storage } from '@google-cloud/storage';
import fs from 'fs';
import path from 'path';
import {uploadBufferToStorage} from "../../src/infrastructure/uploadBufferToStorage";

describe('Google Cloud Storage Emulator', () => {
    const bucketName = 'issuing-invoice';
    const outputFilePath = 'test/issuing-invoice.pdf';
    const localFilePath = './2024_summer_invoice.pdf';
    let buffer: Buffer;
    beforeAll(() => {
        buffer = fs.readFileSync(localFilePath);
    });

    test('should upload file to the emulator', async () => {
        await uploadBufferToStorage(bucketName, outputFilePath, buffer);

        const storage = new Storage({
            projectId: '2024-cloud-storage',
            apiEndpoint: 'http://127.0.0.1:4443',
        });

        const bucket = storage.bucket(bucketName);
        const file = bucket.file(outputFilePath);
        const [exists] = await file.exists();
        expect(exists).toBe(true);

        const [downloadedBuffer] = await file.download();
        // fix to save downloaded file
        fs.writeFileSync(path.join("./", 'downloaded.pdf'), downloadedBuffer);

        expect(downloadedBuffer).toEqual(buffer);
    });
});