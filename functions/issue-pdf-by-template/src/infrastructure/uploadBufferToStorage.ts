import {Storage} from '@google-cloud/storage';
import {getEnv} from "../env";

export const uploadBufferToStorage = async (bucketName: string, outputFilePath: string, buffer: Buffer): Promise<void> => {

    const storage = new Storage({
        projectId: getEnv("GCS_PROJECT_ID",'2024-cloud-storage'),
        apiEndpoint: getEnv("GCLOUD_STORAGE_EMULATOR_HOST",'http://127.0.0.1:4443'), // エミュレーターのエンドポイントを指定
    });
    const bucket = storage.bucket(bucketName);
    const file = bucket.file(outputFilePath );
    const [exists] = await bucket.exists();

    if (!exists) {
        // バケットが存在しない場合、バケットを作成
        await storage.createBucket(bucketName);
        console.log(`Bucket ${bucketName} created.`);
    } else {
        console.log(`Bucket ${bucketName} already exists.`);
    }

    await file.save(buffer, {
        resumable: false,
        preconditionOpts: {ifGenerationMatch: file.generation}
    });
};