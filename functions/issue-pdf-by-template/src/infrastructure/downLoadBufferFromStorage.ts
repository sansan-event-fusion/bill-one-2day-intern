import {Storage} from "@google-cloud/storage";
import {getEnv} from "../env";
import fs from "fs";

//  動作確認用
export const downloadBufferFromStorage = async (bucketName: string, filePath: string, localSavePath: string) => {
    const storage = new Storage({
        projectId: getEnv('GCS_PROJECT_ID', 'default-project-id'),
        apiEndpoint: getEnv('GCLOUD_STORAGE_EMULATOR_HOST', 'http://localhost:4443'),
    });

    try {
        const bucket = storage.bucket(bucketName);
        const file = bucket.file(filePath);

        const [exists] = await file.exists();
        if (!exists) {
            throw new Error(`File ${filePath} does not exist in bucket ${bucketName}`);
        }

        const [buffer] = await file.download();
        console.log(`File ${filePath} downloaded from ${bucketName}.`);

        fs.writeFileSync(localSavePath, buffer);
        console.log(`File saved to ${localSavePath}.`);

        console.log(`File was uploaded to: ${getEnv('GCLOUD_STORAGE_EMULATOR_HOST', 'http://localhost:4443')}/${bucketName}/${filePath}`);



        return buffer;
    } catch (err) {
        console.error('Error downloading file:', JSON.stringify(err, null, 2));
    }
};