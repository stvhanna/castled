import { PipelineWizardStepProps } from "@/app/components/pipeline/PipelineWizard";
import Layout from "@/app/components/layout/Layout";
import React, { useEffect, useState } from "react";
import pipelineService from "@/app/services/pipelineService";
import { usePipelineWizContext } from "@/app/common/context/pipelineWizardContext";
import { PipelineSchemaResponseDto } from "@/app/common/dtos/PipelineSchemaResponseDto";
import bannerNotificationService from "@/app/services/bannerNotificationService";
import { Table } from "react-bootstrap";
import { IconTrash } from "@tabler/icons";
import InputSelect from "@/app/components/forminputs/InputSelect";
import InputField from "@/app/components/forminputs/InputField";
import _ from "lodash";
import { Form, Formik } from "formik";
import InputCheckbox from "@/app/components/forminputs/InputCheckbox";
import Loading from "@/app/components/common/Loading";
import classNames from "classnames";
import {
  FieldMapping,
  PipelineMappingDto,
} from "@/app/common/dtos/PipelineCreateRequestDto";
import ButtonSubmit from "@/app/components/forminputs/ButtonSubmit";
import Placeholder from "react-bootstrap/Placeholder";
import {
  NewPipelineSchemaResponseDto,
  MappingGroupField,
} from "./types/newPipelineSchemaTypes";
import data from "./data/data.json";
import Select from "react-select";
import { title } from "process";
import {MappingFieldsProps, SchemaOptions} from "./types/componentTypes"
import MappingImportantFields from "./components/MappingImportantFields";
import MappingMiscellaneousFields from "./components/MappingMiscellaneousFields";

interface MappingInfo {
  [warehouseKey: string]: {
    appField: string;
    isPrimaryKey: boolean;
  };
}

const PipelineMapping = ({
  curWizardStep,
  steps,
  stepGroups,
  setCurWizardStep,
}: PipelineWizardStepProps) => {
  const { pipelineWizContext, setPipelineWizContext } = usePipelineWizContext();
  const [pipelineSchema, setPipelineSchema] =
    useState<NewPipelineSchemaResponseDto>(data);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    if (!pipelineWizContext) return;
    if (!pipelineWizContext.values) {
      setCurWizardStep("source", "selectType");
      return;
    }
    // pipelineService
    //   .getSchemaForMapping(pipelineWizContext.values)
    //   .then(({ data }) => {
    //     setIsLoading(false);
    //     setPipelineSchema(data);
    //   })
    //   .catch(() => {
    //     setIsLoading(false);
    //     bannerNotificationService.error("Unable to load schemas");
    //   });
  }, [pipelineWizContext?.values]);

  if (!pipelineWizContext) {
    return <Loading />;
  }

  // const appSchemaFields = pipelineSchema?.appSchema?.fields.map((field) => ({
  //   value: field.fieldName,
  //   title: field.fieldName,
  // }));

  // const transformMapping = (mappingInfo: MappingInfo): PipelineMappingDto => {
  //   const fieldMappings: FieldMapping[] = [];
  //   const primaryKeys: string[] = [];
  //   _.each(mappingInfo, (value, key) => {
  //     if (value.appField) {
  //       fieldMappings.push({
  //         warehouseField: key,
  //         appField: value.appField,
  //         skipped: false,
  //       });
  //     }
  //     if (value.isPrimaryKey) {
  //       primaryKeys.push(value.appField);
  //     }
  //   });
  //   return {
  //     primaryKeys,
  //     fieldMappings,
  //   };
  // };

  const initialMappingInfo: MappingInfo = (pipelineWizContext.mappingInfo ||
    {}) as MappingInfo;
  // if (!appSchemaFields) {
  //   pipelineSchema?.warehouseSchema.fields.map(
  //     (field) =>
  //       (initialMappingInfo[field.fieldName] = {
  //         appField: field.fieldName,
  //         isPrimaryKey: false,
  //       })
  //   );
  // }

  const { warehouseSchema, mappingGroups } = data;

  const mandatoryFields = mappingGroups.map((fields) => {
    return fields.type === "IMPORTANT_PARAMS" ? fields.fields : [];
  })[0];

  const miscellaneousFieldGroup = mappingGroups.filter((fields) => {
    return fields.type === "MISCELLANEOUS_FIELDS" && fields;
  });

  const schemaOptions: SchemaOptions[] = warehouseSchema.fields.map((field) => {
    return {
      value: field.fieldName,
      label: field.fieldName,
    };
  });

  return (
    <Layout
      title={steps[curWizardStep].title}
      subTitle={steps[curWizardStep].description}
      centerTitle={true}
      steps={steps}
      stepGroups={stepGroups}
    >
      <div className="table-responsive">
        <Formik
          initialValues={initialMappingInfo}
          onSubmit={(values, { setSubmitting }) => {
            if (!pipelineWizContext.values) return setSubmitting(false);
            pipelineWizContext.mappingInfo = values;
            setPipelineWizContext(pipelineWizContext);
            setCurWizardStep(undefined, "settings");
            setSubmitting(false);
          }}
        >
          {({ values, setFieldValue, setFieldTouched, isSubmitting }) => (
            <Form>
              <div>
                {mandatoryFields?.map((field) => (
                  <MappingImportantFields
                    title={field.title}
                    options={schemaOptions}
                  />
                ))}
              </div>
              <div>
                {miscellaneousFieldGroup?.map((field) => (
                  <MappingMiscellaneousFields
                    title={field.title}
                    options={schemaOptions}
                  />
                ))}
              </div>
              <ButtonSubmit submitting={isSubmitting}>Continue</ButtonSubmit>
            </Form>
          )}
        </Formik>
      </div>
    </Layout>
  );
};

export default PipelineMapping;
