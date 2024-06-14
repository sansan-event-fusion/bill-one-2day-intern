import React, { FC } from "react";
import "@mantine/dropzone/styles.css";
import { Group, Text } from "@mantine/core";
import { Dropzone, MIME_TYPES } from "@mantine/dropzone";
import { CiFileOn } from "react-icons/ci";
import { AiOutlineClose } from "react-icons/ai";
import { MdOutlinePhotoSizeSelectLarge } from "react-icons/md";

type PdfDropZoneProps = {
  onDrop: (files: File[]) => void;
};
export const PdfDropZone: FC<PdfDropZoneProps> = ({ onDrop }) => {
  return (
    <div>
      <Dropzone
        onDrop={(files) => onDrop(files)}
        onReject={(files) => console.log("rejected files", files)}
        maxSize={5 * 1024 ** 2}
        accept={[MIME_TYPES.pdf]}
      >
        <Group
          justify="center"
          gap="xl"
          mih={220}
          style={{ pointerEvents: "none" }}
        >
          <Dropzone.Accept>
            <CiFileOn />
          </Dropzone.Accept>
          <Dropzone.Reject>
            <AiOutlineClose />
          </Dropzone.Reject>
          <Dropzone.Idle>
            <MdOutlinePhotoSizeSelectLarge />
          </Dropzone.Idle>

          <div>
            <Text size="xl" inline>
              ここにファイルをドラッグ＆ドロップ または
              クリックしてファイルを選択
            </Text>
            <Text size="sm" c="dimmed" inline mt={7}>
              ファイルは1件ずつ登録できます
            </Text>
          </div>
        </Group>
      </Dropzone>
    </div>
  );
};
