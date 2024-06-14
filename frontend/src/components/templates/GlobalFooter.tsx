import { Box, Stack } from "@mantine/core";
import "@mantine/core/styles.css";

import { FC } from "react";
export const GlobalFooter: FC = () => {
  return (
    <Box
      style={{
        position: "absolute",
        bottom: 0,
        width: "100vw",
        height: "5vh",
        borderTop: "1px solid #EEEEEE",
      }}
    >
      <Stack>
        <p>&copy; Sansan, Inc.</p>
      </Stack>
    </Box>
  );
};
