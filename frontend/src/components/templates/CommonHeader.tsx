import "@mantine/core/styles.css";

import { Box, Flex } from "@mantine/core";
import { Logo } from "src/components/atoms/Logo";
import { FC } from "react";

export const CommonHeader: FC = () => {
  return (
    <Flex
      h={50}
      style={{ borderBottom: "1px solid #EEEEEE" }}
      justify={"space-between"}
    >
      <Box pt={2} pl={2}>
        <Logo />
      </Box>
    </Flex>
  );
};
